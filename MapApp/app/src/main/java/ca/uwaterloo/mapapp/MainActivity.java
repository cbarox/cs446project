package ca.uwaterloo.mapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {

    private ListView buildingsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildingsListView = (ListView) findViewById(R.id.buildingsListView);
        buildingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent buildingActivityIntent = new Intent(getApplicationContext(), BuildingActivity.class);
                buildingActivityIntent.putExtra(BuildingActivity.EXTRA_BUILDING_ID, position);
                startActivity(buildingActivityIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
