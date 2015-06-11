package ca.uwaterloo.mapapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.objects.Building;
import ca.uwaterloo.mapapp.logic.Buildings;
import ca.uwaterloo.mapapp.logic.Logger;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(43.461340, -80.573), // bottom-left
            new LatLng(43.487251, -80.532603)); // top right
    /**
     * All the actions that are processed by the broadcast receiver
     */
    private static final String[] receiverActions = {
            Buildings.ACTION_BUILDINGS_PROCESSED
    };
    /**
     * This has to be static so it isn't garbage collected when the activity is destroyed
     */
    private static ArrayList<Building> buildingsCache;
    @InjectView(R.id.info_card_layout)
    protected SlidingUpPanelLayout mSlidingLayout;
    // icard info
    @InjectView(R.id.icard_buildName)
    protected TextView mCardBuildName;
    @InjectView(R.id.icard_buildcode)
    protected  TextView mCardBuildCode;
    private GoogleMap mMap;
    // TODO change this to inner class
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            switch (action) {
                case Buildings.ACTION_BUILDINGS_PROCESSED: {
                    // Get the buildings from the intent extras
                    if (extras == null) {
                        Logger.error("No extras supplied from intent %s", Buildings.ACTION_BUILDINGS_PROCESSED);
                        return;
                    }
                    Object buildingsObject = extras.get(Buildings.EXTRA_BUILDINGS);
                    if (buildingsObject == null) {
                        Logger.error("Couldn't get buildings from intent extras");
                        return;
                    }
                    ArrayList<Building> buildings = (ArrayList<Building>) buildingsObject;
                    handleBuildingsProcessed(buildings);
                }
            }
        }
    };

    /**
     * On startup, MainApplication will ask the Waterloo API for a list of buildings, but it takes a while.
     * So when the buildings are finished getting retrieved from the API and added to the database, this method updates the UI.
     * Also local caches the buildings so we don't need to get them again if the activity is destroyed
     *
     * @param buildings List of buildings retrieved from the API
     */
    private void handleBuildingsProcessed(ArrayList<Building> buildings) {
        buildingsCache = buildings;
        addBuildingMarkers(buildingsCache);
    }

    /**
     * Adds all the building markers to the map
     *
     * @param buildings A List of buildings to add
     */
    private void addBuildingMarkers(ArrayList<Building> buildings) {
        for (Building building : buildings) {
            LatLng buildingLocation = new LatLng(building.getLatitude(), building.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(buildingLocation)
                    .title(building.getBuildingName())
                    .snippet(building.getBuildingCode());
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This has to come after setContentView
        ButterKnife.inject(this);

        // Register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        for (String action : receiverActions) {
            intentFilter.addAction(action);
        }
        registerReceiver(broadcastReceiver, intentFilter);

        // Check if we need to get the buildings from the database/API or if we can just use the local cache
        if (buildingsCache == null) {
            // TODO Do this in android service
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Buildings.updateBuildings(getApplicationContext());
                }
            }).start();
        }

        // initialize map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

        mSlidingLayout = (SlidingUpPanelLayout)findViewById(R.id.info_card_layout);
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
            case R.id.action_settings: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // show info card
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                mCardBuildName.setText(marker.getTitle());
                mCardBuildCode.setText(marker.getSnippet());

                showHideInfoCard(true);

                // zoom in and center the camera on the marker
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(marker.getPosition())
                        .zoom(19)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);

                return true; // disable Google marker popup
            }
        });
        // hide info card
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showHideInfoCard(false);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                correctVisibleRegion();
            }
        });

        if (buildingsCache != null) {
            addBuildingMarkers(buildingsCache);
        }
    }

    /**
     * Shows or hides the info card
     *
     * @param enabled Whether to show the info card
     */
    private void showHideInfoCard(boolean enabled) {
        mSlidingLayout.setPanelState(enabled ? PanelState.COLLAPSED : PanelState.HIDDEN);
    }

    /**
     * Returns the correction for Lat and Lng if camera is trying to get outside of visible map
     *
     * @param cameraBounds Current camera bounds
     * @return Latitude and Longitude corrections to get back into bounds.
     */
    private LatLngBounds getCorrectedRegion(LatLngBounds cameraBounds) {
        double boundsWidth = Math.abs(BOUNDS.northeast.latitude - BOUNDS.southwest.latitude);
        double boundsHeight = Math.abs(BOUNDS.northeast.longitude - BOUNDS.southwest.longitude);
        double cameraWidth = Math.abs(cameraBounds.northeast.latitude - cameraBounds.southwest.latitude);
        double cameraHeight = Math.abs(cameraBounds.northeast.longitude - cameraBounds.southwest.longitude);
        double cameraAspectRatio = cameraWidth / cameraHeight;

        // Use the camera aspect ratio to make a new bounds that has the same aspect ratio
        LatLng adjustedSWBound, adjustedNEBound;
        if (cameraAspectRatio > 1) {
            double adjustedBoundsWidth = boundsHeight * cameraAspectRatio;
            adjustedSWBound = new LatLng(BOUNDS.getCenter().latitude - adjustedBoundsWidth / 2, BOUNDS.southwest.longitude);
            adjustedNEBound = new LatLng(BOUNDS.getCenter().latitude + adjustedBoundsWidth / 2, BOUNDS.northeast.longitude);
        } else {
            double adjustedBoundsHeight = boundsWidth / cameraAspectRatio;
            adjustedSWBound = new LatLng(BOUNDS.southwest.latitude, BOUNDS.getCenter().longitude - adjustedBoundsHeight / 2);
            adjustedNEBound = new LatLng(BOUNDS.northeast.latitude, BOUNDS.getCenter().longitude + adjustedBoundsHeight / 2);
        }

        // Check if the user is zoomed out too far
        LatLngBounds adjustedBounds = new LatLngBounds(adjustedSWBound, adjustedNEBound);
        double adjustedBoundsWidth = Math.abs(adjustedBounds.northeast.latitude - adjustedBounds.southwest.latitude);
        double adjustedBoundsHeight = Math.abs(adjustedBounds.northeast.longitude - adjustedBounds.southwest.longitude);
        if (adjustedBoundsWidth < cameraWidth) {
            return adjustedBounds;
        } else if (adjustedBoundsHeight < cameraHeight) {
            return adjustedBounds;
        }

        // Fix latitude
        double deltaLat = 0;
        if (cameraBounds.southwest.latitude < adjustedBounds.southwest.latitude) {
            deltaLat = adjustedBounds.southwest.latitude - cameraBounds.southwest.latitude;
        } else if (cameraBounds.northeast.latitude > adjustedBounds.northeast.latitude) {
            deltaLat = adjustedBounds.northeast.latitude - cameraBounds.northeast.latitude;
        }
        double swLat = cameraBounds.southwest.latitude + deltaLat;
        double neLat = cameraBounds.northeast.latitude + deltaLat;

        // Fix longitude
        double deltaLong = 0;
        if (cameraBounds.southwest.longitude < adjustedBounds.southwest.longitude) {
            deltaLong = adjustedBounds.southwest.longitude - cameraBounds.southwest.longitude;
        } else if (cameraBounds.northeast.longitude > adjustedBounds.northeast.longitude) {
            deltaLong = adjustedBounds.northeast.longitude - cameraBounds.northeast.longitude;
        }
        double swLong = cameraBounds.southwest.longitude + deltaLong;
        double neLong = cameraBounds.northeast.longitude + deltaLong;

        return new LatLngBounds(new LatLng(swLat, swLong), new LatLng(neLat, neLong));
    }

    /**
     * Bounds the user to the overlay.
     */
    public void correctVisibleRegion() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLngBounds correctedRegion = getCorrectedRegion(visibleRegion.latLngBounds);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(correctedRegion, 0);
        mMap.animateCamera(cameraUpdate, 300, null);
    }
}
