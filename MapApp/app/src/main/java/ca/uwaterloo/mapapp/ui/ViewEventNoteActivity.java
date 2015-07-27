package ca.uwaterloo.mapapp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;

public class ViewEventNoteActivity extends ActionBarActivity {
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    public static final String ARG_EVENT_NOTE_ID = "ARG_EVENT_NOTE_ID";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;
    @InjectView(R.id.event_note_title)
    protected TextView noteTitle;
    @InjectView(R.id.event_note_description)
    protected TextView noteDesc;

    private int eventId;
    private long eventNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_note);
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
        eventId = b.getInt(ARG_EVENT_ID, -1);
        eventNoteId = b.getLong(ARG_EVENT_NOTE_ID, -1);
        if (eventId <= 0 || eventNoteId <= 0)
            onBackPressed();

        ServerRestApi.requestEventNotes(loadEventNotesCallback, eventId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private ICallback loadEventNotesCallback = new ICallback() {
        @Override
        public void call(Object param) {
            List<EventNote> notes = (List<EventNote>) param;
            for (EventNote note : notes) {
                if (note.getId() == eventNoteId) {
                    noteTitle.setText(note.getTitle());
                    noteDesc.setText(note.getDescription());
                    break;
                }
            }
        }
    };
}
