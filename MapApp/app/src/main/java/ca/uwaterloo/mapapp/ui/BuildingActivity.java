package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;

public class BuildingActivity extends AppCompatActivity {

    // Intent Extras for this activity
    public static final String EXTRA_BUILDING_ID = "EXTRA_BUILDING_ID";

    @InjectView(R.id.nameTextView)
    protected TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        // This has to come after setContentView
        ButterKnife.inject(this);

        // Get info from intent extras
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int buildingId = -1;
        if (extras != null) {
            if (extras.keySet().contains(EXTRA_BUILDING_ID)) {
                buildingId = extras.getInt(EXTRA_BUILDING_ID);
            }
        }

        String buildingCode = getResources().getStringArray(R.array.building_codes)[buildingId];
        setTitle(buildingCode);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(String.format("%s Notes", buildingCode));
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu Menu that's being inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_building, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here.
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
}
