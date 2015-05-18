package ca.uwaterloo.mapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

@SuppressWarnings("FieldCanBeLocal")
public class BuildingActivity extends AppCompatActivity {

    public static final String EXTRA_BUILDING = "building";

    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);


        // Get building name from intent extras
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String buildingName = "Error Getting Building Name";
        if (extras != null) {
            buildingName = extras.getString(EXTRA_BUILDING);
        }

        setTitle(buildingName);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(buildingName + " Notes");
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
