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

@SuppressWarnings("FieldCanBeLocal")
public class BuildingActivity extends AppCompatActivity {

    public static final String EXTRA_BUILDING_ID = "EXTRA_BUILDING_ID";

    @InjectView(R.id.nameTextView)
    protected TextView nameTextView;

    private String buildingCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
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

        buildingCode = getResources().getStringArray(R.array.building_codes)[buildingId];

        setTitle(buildingCode);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(String.format("%s Notes", buildingCode));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_building, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
