package ca.uwaterloo.mapapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;

import java.util.List;

import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.logic.Logger;
import ca.uwaterloo.mapapp.logic.net.WaterlooApi;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainDrawerActivity extends MaterialNavigationDrawer {

    /**
     * All the actions that are processed by the broadcast receiver
     */
    private static final String[] receiverActions = {
            WaterlooApi.ACTION_GOT_LIST
    };

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

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            switch (action) {
                case WaterlooApi.ACTION_GOT_LIST: {
                    if (extras == null) {
                        Logger.error("No extras supplied from intent %s", WaterlooApi.ACTION_GOT_LIST);
                        return;
                    }
                    List list = (List) extras.get(WaterlooApi.EXTRA_LIST);
                    if (list == null) {
                        Logger.error("Couldn't get list from intent extras");
                        return;
                    }
                    String className = extras.getString(WaterlooApi.EXTRA_CLASS);
                    if (className == null) {
                        Logger.error("Couldn't get class name from intent extras");
                        return;
                    }
                    if (className.equals("Building")) {
                        mainMapFragment.handleGotBuildings(list);
                        allBuildingsFragment.handleGotBuildings(list);
                    } else if (className.equals("Event")) {
                        allEventsFragment.handleGotEvents(list);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        for (String action : receiverActions) {
            intentFilter.addAction(action);
        }
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void init(Bundle savedInstanceState) {

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
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        if (getCurrentSection().equals(mainMapFragment)) {
            handled = ((MainMapFragment)mainMapFragment.getTargetFragment()).showHideInfoCard(false);
        }
        if (!handled) {
            super.onBackPressed();
        }
    }
}
