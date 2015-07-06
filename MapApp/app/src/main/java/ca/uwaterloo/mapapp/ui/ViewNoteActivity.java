package ca.uwaterloo.mapapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.ui.adapters.NotePropertyAdapter;

public class ViewNoteActivity extends ActionBarActivity {

    public static final String ARG_NOTE_ID = "note_id";
    public static final String RESULT_NOTE_ID = "result_note_id";

    public static final int REQUEST_UPDATE = 102;

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.note_title)
    protected TextView mTitle;
    @InjectView(R.id.note_info_list)
    protected ListView mProperties;

    private Note mNote = null;
    private boolean hasUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
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
        long noteId = b.getLong(ARG_NOTE_ID, -1);
        setNote(noteId);

        // set up edit button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewNoteActivity.this, NewEditNoteActivity.class);
                intent.putExtra(ViewNoteActivity.ARG_NOTE_ID, mNote.getId());
                startActivityForResult(intent, NewEditNoteActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    @Override
    public void finish() {
        if (hasUpdated) {
            Intent data = new Intent();
            data.putExtra(RESULT_NOTE_ID, mNote.getId());
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) return;

        if (requestCode == NewEditNoteActivity.REQUEST_UPDATE) {
            long noteId = data.getLongExtra(NewEditNoteActivity.RESULT_NOTE_ID, -1);
            setNote(noteId);
            hasUpdated = true;
        }
    }

    /**
     * Updates the view to display the new note
     * @param noteId
     */
    private void setNote(long noteId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
        mNote = dataManager.findById(noteId);

        mTitle.setText(mNote.getTitle());
        mProperties.setAdapter(new NotePropertyAdapter(this, mNote));
    }
}
