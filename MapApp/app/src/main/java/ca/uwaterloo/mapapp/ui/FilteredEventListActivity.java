package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.ui.adapters.FilteredEventAdapter;

public class FilteredEventListActivity extends ActionBarActivity {
    public static final String ARG_FILTER_VALUE_STRING = "ARG_FILTER_STRING";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.event_list)
    protected ListView mEventList;

    private List<Event> eventList;
    private FilteredEventAdapter mAdapter;
    private String filterString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_event_list);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle b = getIntent().getExtras();
        String filterName = "";

        // Load the list of events by building code
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        filterString = b.getString(ARG_FILTER_VALUE_STRING, "");
        filterName = filterString;
        DataManager<Event, Long> dataManager = databaseHelper.getDataManager(Event.class);
        eventList = dataManager.find(Event.COLUMN_LOCATION, filterString);

        mAdapter = new FilteredEventAdapter(this, eventList, filterName);
        mEventList.setAdapter(mAdapter);
        mEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id < 0) return; // without this it crashed if you clicked the title

                Intent intent = new Intent(FilteredEventListActivity.this, ViewEventActivity.class);
                intent.putExtra(ViewEventActivity.ARG_EVENT_ID, id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return false; }
}
