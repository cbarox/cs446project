package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainDrawerActivity extends MaterialNavigationDrawer {

   @Override
   public void init(Bundle savedInstanceState) {
      this.addSection(newSection("Campus Map", new MainMapFragment()));
      this.addDivisor();

       this.addSection(newSection("All Events", new AllEventsFragment()));
       this.addSection(newSection("All Notes", new AllNotesFragment()));
       this.addSection(newSection("All Buildings", new AllBuildingsFragment()));
       this.addSection(newSection("All Tags", new AllTagsFragment()));

       this.addBottomSection(newSection("Settings", new Intent(this, SettingsActivity.class)));
   }


}
