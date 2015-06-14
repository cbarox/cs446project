package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import ca.uwaterloo.mapapp.R;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainDrawerActivity extends MaterialNavigationDrawer {

    private MaterialSection campusMapFrag;
    private MaterialSection allEventsFrag;
    MaterialSection allNotesFrag;
    MaterialSection allBuildFrag;
    MaterialSection allTagsFrag;

   @Override
   public void init(Bundle savedInstanceState) {

       Resources res = getResources();

       campusMapFrag = newSection("Campus Map", new MainMapFragment());
       this.addSection(campusMapFrag);
       this.addDivisor();

       allEventsFrag = newSection("All Events",
               res.getDrawable(R.drawable.ic_announcement_black_24dp),
               new AllEventsFragment());
       this.addSection(allEventsFrag);
       allNotesFrag = newSection("All Notes", new AllNotesFragment());
       this.addSection(allNotesFrag);
       allBuildFrag = newSection("All Buildings", new AllBuildingsFragment());
       this.addSection(allBuildFrag);
       allTagsFrag = newSection("All Tags", new AllTagsFragment());
       this.addSection(allTagsFrag);

       this.addBottomSection(newSection("Settings",
               res.getDrawable(R.drawable.ic_settings_black_24dp),
               new Intent(this, SettingsActivity.class)));

       setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
   }

    @Override
    public void onBackPressed() {
        if (getCurrentSection().equals(campusMapFrag)) {

        } else {
            super.onBackPressed();
        }
    }
}
