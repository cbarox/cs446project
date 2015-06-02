package ca.uwaterloo.mapapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import butterknife.ButterKnife;
import ca.uwaterloo.mapapp.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final LatLngBounds BOUNDS = new LatLngBounds(
            // bottom left                    top right
            new LatLng(43.461340, -80.573), new LatLng(43.487251, -80.532603));
    private final int MIN_ZOOM = 15;
    private final int REFRESH_TIME = 100; //ms
    private OverscrollHandler mOverscrollHandler = new OverscrollHandler();
    private GoogleMap mMap;

    // TEMP
    private LatLng M3Location = new LatLng(43.473211, -80.544131);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This has to come after setContentView
        ButterKnife.inject(this);

        // initialize map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu Menu that's being inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here.
     *
     * @param item Item that was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // MAP FUNCTIONS

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        mMap.addMarker(new MarkerOptions()
            .position(M3Location)
        .title("M3"));

        mOverscrollHandler.sendEmptyMessageDelayed(0,REFRESH_TIME);
    }

    /**
     * Returns the correction for Lat and Lng if camera is trying to get outside of visible map
     * @param cameraBounds Current camera bounds
     * @return Latitude and Longitude corrections to get back into bounds.
     */
    private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude=0, longitude=0;
        if(cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

    /**
     * Bounds the user to the overlay.
     */
    private class OverscrollHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            CameraPosition position = mMap.getCameraPosition();
            VisibleRegion region = mMap.getProjection().getVisibleRegion();
            float zoom = 0;
            if(position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
            LatLng correction = getLatLngCorrection(region.latLngBounds);
            if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
                zoom = (zoom==0)?position.zoom:zoom;
                double lat = position.target.latitude + correction.latitude;
                double lon = position.target.longitude + correction.longitude;
                CameraPosition newPosition = new CameraPosition(new LatLng(lat,lon), zoom, position.tilt, position.bearing);
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
                mMap.moveCamera(update);
            }
            /* Recursively call handler */
            sendEmptyMessageDelayed(0,REFRESH_TIME);
        }
    }
}
