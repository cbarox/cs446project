package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.Event;

public class ViewEventActivity extends ActionBarActivity {
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;
    @InjectView(R.id.event_title)
    protected TextView title;
    @InjectView(R.id.event_location_icon)
    protected ImageView locIcon;
    @InjectView(R.id.event_location_txt)
    protected TextView location;
    @InjectView(R.id.event_link_btn)
    protected Button link;

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
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
        long eventId = b.getLong(ARG_EVENT_ID, -1);
        setEvent(eventId);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return false; }

    private void setEvent(long eventId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Event, Long> dataManager = databaseHelper.getDataManager(Event.class);

        mEvent = dataManager.findFirst(Event.COLUMN_ID, eventId);
        title.setText(mEvent.getTitle());

        if (mEvent.getLocation() != null && !mEvent.getLocation().isEmpty()) {
            location.setText(mEvent.getLocation());

        } else {
            location.setText("University of Waterloo");
        }

        if (mEvent.getLink() != null && !mEvent.getLink().isEmpty()) {
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = mEvent.getLink();
                    if (!link.startsWith("https://") && !link.startsWith("http://")) {
                        link = "http://" + link;
                    }

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });

        } else {
            link.setVisibility(View.INVISIBLE);
        }
    }
}
