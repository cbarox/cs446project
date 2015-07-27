package ca.uwaterloo.mapapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;

public class AddEventNoteActivity extends ActionBarActivity {
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    public static final int REQUEST_INSERT = 501;

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;
    @InjectView(R.id.event_note_title)
    protected EditText noteTitle;
    @InjectView(R.id.event_note_description)
    protected EditText noteDesc;

    private Integer eventId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_note);
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
        if (eventId <= 0)
            goBack(false);

        noteTitle.setText("");

        if (noteTitle.requestFocus()) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        EventNote note = new EventNote();
        note.setEventId(eventId);
        note.setTitle(noteTitle.getText().toString());
        note.setDescription(noteDesc.getText().toString());

        if (note.getTitle().isEmpty() && note.getDescription().isEmpty()) {
            finish();
            return;

        } else if (note.getTitle().isEmpty()) {
            if (note.getDescription().length() > 15) {
                note.setTitle(note.getDescription().substring(0, 16) + "...");
            } else {
                note.setTitle(note.getDescription());
            }
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Note ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        ServerRestApi.addOrSetEventNote(uploadedEventNoteCallback, note);

    }

    private ICallback uploadedEventNoteCallback = new ICallback() {
        @Override
        public void call(Object param) {
            progressDialog.dismiss();
            goBack(true);
        }
    };

    private void goBack(boolean result) {
        if (result) {
            Intent data = new Intent();
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
