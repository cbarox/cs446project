package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import ca.uwaterloo.mapapp.R;

public class MainActivity extends AppCompatActivity {

    // Temporary ListView
    @InjectView(R.id.buildingsListView)
    protected ListView buildingsListView;

    @OnItemClick(R.id.buildingsListView)
    void buildingClicked(int position) {
        Intent buildingActivityIntent = new Intent(getApplicationContext(), BuildingActivity.class);
        buildingActivityIntent.putExtra(BuildingActivity.EXTRA_BUILDING_ID, position);
        startActivity(buildingActivityIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This has to come after setContentView
        ButterKnife.inject(this);


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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
