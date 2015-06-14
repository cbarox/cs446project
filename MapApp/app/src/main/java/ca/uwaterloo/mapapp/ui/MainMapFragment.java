package ca.uwaterloo.mapapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.logic.net.WaterlooApi;
import ca.uwaterloo.mapapp.logic.net.objects.Building;

public class MainMapFragment extends Fragment implements OnMapReadyCallback {

    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(43.451340, -80.583), // bottom-left
            new LatLng(43.497251, -80.522603)); // top right

    /**
     * This has to be static so it isn't garbage collected when the activity is destroyed
     */
    private static List<Building> buildingsCache;
    @InjectView(R.id.info_card_layout)
    protected SlidingUpPanelLayout mSlidingLayout;
    // icard info
    @InjectView(R.id.icard_buildName)
    protected TextView mCardBuildName;
    @InjectView(R.id.icard_buildcode)
    protected TextView mCardBuildCode;
    private Context context;
    private GoogleMap mMap;


    /**
     * On startup, MainApplication will ask the Waterloo API for a list of buildings, but it takes a while.
     * So when the buildings are finished getting retrieved from the API, this method updates the UI.
     * Also local caches the buildings so we don't need to get them again if the activity is destroyed
     *
     * @param buildings List of buildings retrieved from the API
     */
    public void handleGotBuildings(List<Building> buildings) {
        buildingsCache = buildings;
        addBuildingMarkers(buildingsCache);
    }

    /**
     * Adds all the building markers to the map
     *
     * @param buildings A List of buildings to add
     */
    private void addBuildingMarkers(List<Building> buildings) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = this.getActivity();
        // This has to come after setContentView
        ButterKnife.inject(this, view);


        // Check if we need to get the buildings from the database/API or if we can just use the local cache
        if (buildingsCache == null) {
            final Context context = this.getActivity();
            WaterlooApi.requestList(context, Building.class);
        }

        // initialize map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setup Fab action
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_new_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewNoteActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        destroyMap();
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        destroyMap();
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

    private void destroyMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().remove(mapFragment).commit();
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
