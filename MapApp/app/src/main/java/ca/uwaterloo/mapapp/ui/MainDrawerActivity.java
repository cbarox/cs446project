package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainDrawerActivity extends MaterialNavigationDrawer {

    private MaterialSection campusMapSection;
    private MaterialSection allEventsSection;
    private MaterialSection allNotesSection;
    private MaterialSection allBuildingsSection;
    private MaterialSection allTagsSections;

    private MainMapFragment mainMapFragment;
    private AllEventsFragment allEventsFragment;
    private AllNotesFragment allNotesFragment;
    private AllBuildingsFragment allBuildingsFragment;
    private AllTagsFragment allTagsFragment;

    /**
     * Refreshes all the data for the application
     * This takes a long time to run
     * TODO check if we even need to refresh based on time or something
     * TODO add a progress dialog for this
     */
    private void refreshData() {

        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();

        // Cache buildings
        ICallback buildingsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                DataManager<Building, String> buildingsDataManager = databaseHelper.getDataManager(Building.class);
                List<Building> buildings = (List<Building>) param;
                buildingsDataManager.insertOrUpdateAll(buildings);
            }
        };
        WaterlooApi.requestBuildings(buildingsCallback);

        // Cache events
        ICallback eventsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                DataManager<Event, Integer> eventDataManager = databaseHelper.getDataManager(Event.class);
                List<Event> events = (List<Event>) param;
                eventDataManager.insertOrUpdateAll(events);
            }
        };
        WaterlooApi.requestEvents(eventsCallback);

        // TODO implement this
        // Cache event locations
        ICallback eventLocationsCallback = new ICallback() {
            @Override
            public void call(Object param) {

            }
        };
        // TODO add rest service for our server to request from
    }

    @Override
    public void init(Bundle savedInstanceState) {

        refreshData();

        // setup drawer image
        setDrawerHeaderImage(R.drawable.test_drawer_image);

        Resources res = getResources();

        mainMapFragment = new MainMapFragment();
        campusMapSection = newSection("Campus Map",
                res.getDrawable(R.drawable.ic_map_black_24dp),
                mainMapFragment);
        this.addSection(campusMapSection);
        this.addDivisor();

        allEventsFragment = new AllEventsFragment();
        allEventsSection = newSection("All Events",
                res.getDrawable(R.drawable.ic_announcement_black_24dp),
                allEventsFragment);
        this.addSection(allEventsSection);

        allNotesFragment = new AllNotesFragment();
        allNotesSection = newSection("All Notes",
                res.getDrawable(R.drawable.ic_description_black_24dp),
                allNotesFragment);
        this.addSection(allNotesSection);

        allBuildingsFragment = new AllBuildingsFragment();
        allBuildingsSection = newSection("All Buildings",
                res.getDrawable(R.drawable.ic_store_black_24dp),
                allBuildingsFragment);
        this.addSection(allBuildingsSection);

        allTagsFragment = new AllTagsFragment();
        allTagsSections = newSection("All Tags",
                res.getDrawable(R.drawable.ic_local_offer_black_24dp),
                allTagsFragment);
        this.addSection(allTagsSections);

        this.addBottomSection(newSection("Settings",
                res.getDrawable(R.drawable.ic_settings_black_24dp),
                new Intent(this, SettingsActivity.class)));

        setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        setDefaultSectionLoaded(0);
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        if (getCurrentSection().equals(mainMapFragment)) {
            handled = ((MainMapFragment) mainMapFragment.getTargetFragment()).showHideInfoCard(false);
        }
        if (!handled) {
            super.onBackPressed();
        }
    }
}
