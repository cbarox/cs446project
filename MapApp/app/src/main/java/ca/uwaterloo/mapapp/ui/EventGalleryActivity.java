package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.ui.adapters.EventImageAdapter;

public class EventGalleryActivity extends ActionBarActivity {
    public static final String ARG_FILTER_VALUE_INTEGER = "ARG_FILTER_INTEGER";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.image_grid)
    protected GridView mImageGrid;

    private List<EventImage> imageList;
    private EventImageAdapter mAdapter;
    private Integer eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallery);
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

        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id < 0) return; // without this it crashed if you clicked the title

                Intent intent = new Intent(EventGalleryActivity.this, DisplayImageActivity.class);
                intent.putExtra(DisplayImageActivity.ARG_IMAGE, imageList.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        // Load the list of events by building code
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        eventId = b.getInt(ARG_FILTER_VALUE_INTEGER, 0);
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                imageList = (List<EventImage>) param;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new EventImageAdapter(EventGalleryActivity.this, imageList);
                        mImageGrid.setAdapter(mAdapter);
                    }
                });

            }
        };
        ServerRestApi.requestEventImages(callback, eventId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return false; }
}
