package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.objects.NoteTag;
import ca.uwaterloo.mapapp.objects.Tag;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.ui.adapters.FilteredNoteAdapter;

public class FilteredNoteListActivity extends ActionBarActivity {
    public static final String ARG_FILTER_TYPE = "ARG_FILTER_TYPE";
    public static final String ARG_FILTER_BUILDING_CODE = "ARG_BUILDING_CODE";
    public static final String ARG_FILTER_ROOM_NUMBER = "ARG_ROOM_NUMBER";
    public static final String ARG_FILTER_TAG_ID = "ARG_TAG_ID";

    public static final int FILTER_BUILDING = 1;
    public static final int FILTER_TAGS = 2;

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.note_list)
    protected ListView mNoteList;
    @InjectView(R.id.fab_new_note)
    protected FloatingActionButton fab;

    private List<Note> noteList;
    private FilteredNoteAdapter mAdapter;
    private int filterType;
    private String filterBuildCode;
    private String filterRoomNumber;
    private long filterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_note_list);
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
        filterType = b.getInt(ARG_FILTER_TYPE, 0);
        String filterName = "";

        // Load the list of notes
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        if (filterType == FILTER_BUILDING) {
            filterBuildCode = b.getString(ARG_FILTER_BUILDING_CODE, "");
            filterRoomNumber = b.getString(ARG_FILTER_ROOM_NUMBER, "");
            filterName = filterBuildCode;
            DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
            if (!filterRoomNumber.isEmpty()) {
                noteList = dataManager.find(new String[]{Note.COLUMN_BUILDING_CODE, Note.COLUMN_ROOM_NUMBER},
                        new Object[]{filterName, filterRoomNumber}, Note.COLUMN_LAST_MODIFIED, false);
                filterName += " " + filterRoomNumber;
            } else {
                noteList = dataManager.find(Note.COLUMN_BUILDING_CODE, filterBuildCode,
                        Note.COLUMN_LAST_MODIFIED, false);
            }

        } else if (filterType == FILTER_TAGS) {
            filterId = b.getLong(ARG_FILTER_TAG_ID, -1);
            DataManager<Tag, Long> dataManager = databaseHelper.getDataManager(Tag.class);
            Tag tag = dataManager.findById(filterId);
            filterName = "#" + tag.getTitle();

            DataManager<NoteTag, Long> dm2 = databaseHelper.getDataManager(NoteTag.class);
            List<NoteTag> noteTags = dm2.find(NoteTag.COLUMN_TAG, tag);

            noteList = new ArrayList<>(noteTags.size());

            for (NoteTag noteTag : noteTags) {
                noteList.add(noteTag.getNote());
            }

            if (noteList != null) {
                Collections.sort(noteList, new Comparator<Note>() {
                    @Override
                    public int compare(Note lhs, Note rhs) {
                        return rhs.getLastModified().compareTo(lhs.getLastModified());
                    }
                });
            }
        }

        mAdapter = new FilteredNoteAdapter(this, noteList, filterName);
        mNoteList.setAdapter(mAdapter);
        mNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FilteredNoteListActivity.this, ViewNoteActivity.class);
                intent.putExtra(NewEditNoteActivity.ARG_NOTE_ID, id);
                startActivityForResult(intent, ViewNoteActivity.REQUEST_UPDATE);
                overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        fab.attachToListView(mNoteList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilteredNoteListActivity.this, NewEditNoteActivity.class);
                if (filterType == FILTER_BUILDING) {
                    intent.putExtra(NewEditNoteActivity.ARG_SELECTED_BUILD, filterBuildCode);
                    intent.putExtra(NewEditNoteActivity.ARG_SELECTED_ROOM, filterRoomNumber);
                } else if (filterType == FILTER_TAGS) {
                    intent.putExtra(NewEditNoteActivity.ARG_TAG_ID, filterId);
                }
                startActivityForResult(intent, NewEditNoteActivity.REQUEST_INSERT);
                overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return false; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return;

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
        Note note = dataManager.findById(data.getLongExtra(NewEditNoteActivity.RESULT_NOTE_ID, -1));

        if (requestCode == NewEditNoteActivity.REQUEST_INSERT) {
            if (filterType == FILTER_BUILDING) {
                if (note.getBuildingCode().equals(filterBuildCode)) {
                    if (!filterRoomNumber.isEmpty()) {
                        if (note.getRoomNumber().equals(filterRoomNumber)) {
                            noteList.add(0, note);
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        noteList.add(0, note);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            } else if (filterType == FILTER_TAGS) {
                Tag tmp = new Tag();
                tmp.setId(filterId);
                DataManager<NoteTag, Long> dm2 = databaseHelper.getDataManager(NoteTag.class);
                NoteTag noteTag = dm2.findFirst(new String[]{NoteTag.COLUMN_NOTE, NoteTag.COLUMN_TAG},
                        new Object[]{note, tmp});
                if (noteTag != null) {
                    noteList.add(0, note);
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else if (requestCode == NewEditNoteActivity.REQUEST_UPDATE) {
            // remove original
            for (int i = 0; i < noteList.size(); i++) {
                if (note.getId() == noteList.get(i).getId()) {
                    noteList.remove(i);
                    break;
                }
            }

            if (filterType == FILTER_BUILDING) {
                if (note.getBuildingCode().equals(filterBuildCode)) {
                    noteList.add(0, note);
                }

            } else if (filterType == FILTER_TAGS) {
                Tag tmp = new Tag();
                tmp.setId(filterId);
                DataManager<NoteTag, Long> dm2 = databaseHelper.getDataManager(NoteTag.class);
                NoteTag noteTag = dm2.findFirst(new String[]{NoteTag.COLUMN_NOTE, NoteTag.COLUMN_TAG},
                        new Object[]{note, tmp});
                if (noteTag != null) {
                    noteList.add(0, note);
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    }
}
