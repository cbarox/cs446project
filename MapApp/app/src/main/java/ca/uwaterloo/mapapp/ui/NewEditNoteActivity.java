package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.objects.NoteTag;
import ca.uwaterloo.mapapp.objects.Tag;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;

public class NewEditNoteActivity extends ActionBarActivity {

    public static final String ARG_SELECTED_BUILD = "selected_building";
    public static final String ARG_NOTE_ID = "note_id";
    public static final String ARG_TAG_ID = "tag_id";

    public static final int REQUEST_INSERT = 101;
    public static final int REQUEST_UPDATE = 102;

    public static final String RESULT_NOTE_ID = "result_note_id";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.note_title)
    protected EditText mTitle;
    @InjectView(R.id.note_description)
    protected EditText mDescription;
    @InjectView(R.id.note_building)
    protected Button mBuildingBtn;
    @InjectView(R.id.note_tags)
    protected Button mTagsBtn;

    private List<Building> buildingList;
    private String[] buildingNames;
    private int selectedBuildingIndex = 0;
    private String selectedBuildCode = "";

    private List<Tag> masterTagList;
    private String[] tagNames;
    private Integer[] selectedTagPos;
    private List<NoteTag> orgTagList;

    private boolean isUpdate = false;
    private Note mNote = null;

    private void handleGotBuildings(List<Building> buildings) {
        buildingList = buildings;
        buildingNames = new String[buildingList.size() + 1];
        buildingNames[0] = "No building";
        int current = 1;
        for (Building building : buildingList) {
            buildingNames[current] = building.getBuildingName();
            if (building.getBuildingCode().equals(selectedBuildCode)) {
                selectedBuildingIndex = current;
            }
            current++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_note);
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

        ICallback buildingCallback = new ICallback() {
            @Override
            public void call(Object param) {
                List<Building> buildingList = (List<Building>) param;
                handleGotBuildings(buildingList);
            }
        };
        WaterlooApi.requestBuildings(buildingCallback);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            initExtras(b);
        }

        if (mNote != null) {
            isUpdate = true;
            selectedBuildCode = mNote.getBuildingCode();
            if (selectedBuildCode != null && !selectedBuildCode.isEmpty()) {
                mBuildingBtn.setText(selectedBuildCode);
            } else {
                mBuildingBtn.setText(getString(R.string.note_no_building));
            }

            mTitle.setText(mNote.getTitle());
            mDescription.setText(mNote.getDescription());
        } else {
            mDescription.setText("");
            if (selectedBuildCode.isEmpty()) {
                mBuildingBtn.setText(getString(R.string.note_no_building));
            }

            // give focus to title text
            if (mTitle.requestFocus()) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

        if (!selectedBuildCode.isEmpty() && !buildingList.isEmpty()) {
            int current = 0;
            for (Building building : buildingList) {
                if (building.getBuildingCode().equals(selectedBuildCode)) {
                    selectedBuildingIndex = current;
                    break;
                }
                current++;
            }
        }

        initTags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /**
     * Returns the result (ID of the note) back to the previous activity
     */
    public void returnResult(boolean hasUpdatedInserted) {
        if (hasUpdatedInserted) {
            Intent data = new Intent();
            data.putExtra(RESULT_NOTE_ID, mNote.getId());
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    /**
     * Inserts/Updates a non-blank note and returns to the previous activity.<br>
     * If the note already exists then it prompts the user to confirm the update, may not return<br>
     * If the exists and is now empty, it will give it a new title (Untitled note).
     */
    @Override
    public void onBackPressed() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        boolean confirmUpdate = mNote != null;

        if (mNote == null) {
            mNote = new Note();
        }
        final Note org = mNote.getCopy();

        mNote.setTitle(title);
        if (title.isEmpty() && !description.isEmpty()) {
            if (description.length() > 15) {
                mNote.setTitle(description.substring(0, 16) + "...");
            } else {
                mNote.setTitle(description);
            }
        }
        mNote.setDescription(description);

        if (selectedBuildingIndex > 0) {
            mNote.setBuildingCode(mBuildingBtn.getText().toString());
        } else {
            mNote.setBuildingCode("");
        }

        // confirm update of existing note
        // check tag updates
        if (confirmUpdate && (!org.equals(mNote) || !tagListEqual())) {
            new MaterialDialog.Builder(this)
                    .content("Save or discard draft")
                    .positiveText("SAVE")
                    .negativeText("DISCARD")
                    .neutralText("CANCEL")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            updateInsertNote();
                            returnResult(true);
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            returnResult(false);
                        }

                        @Override
                        public void onNeutral(MaterialDialog dialog) {
                            mNote = org;
                        }
                    }).show();

            // no update required
        } else if (org.equals(mNote)) {
            returnResult(false);

            // new note
        } else if (mNote != null && mNote.getTitle() != null && !mNote.getTitle().isEmpty()) {
            updateInsertNote();
            returnResult(true);

            // blank note on insert
        } else {
            returnResult(false);
        }
    }

    private void updateInsertNote() {
        // Insert note into database
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);

        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        mNote.setLastModified(new Date(currentTime));

        if (mNote.getId() > 0) {
            dataManager.update(mNote);
        } else {
            mNote.setDateCreated(new Date(currentTime));
            dataManager.insert(mNote);
        }

        // insert tags
        DataManager<NoteTag, Long> dm2 = databaseHelper.getDataManager(NoteTag.class);
        dm2.deleteAll(orgTagList);

        for (int pos : selectedTagPos) {
            Tag tag = masterTagList.get(pos);
            NoteTag nt = new NoteTag();
            nt.setNote(mNote);
            nt.setTag(tag);
            dm2.insert(nt);
        }

        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }

    public void selectNewBuilding(View view) {
        if (buildingList != null && buildingList.size() > 0) {
            new MaterialDialog.Builder(this)
                    .title("Choose building")
                    .items(buildingNames)
                    .itemsCallbackSingleChoice(selectedBuildingIndex,
                            new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog materialDialog, View view, int which,
                                                           CharSequence charSequence) {
                                    selectedBuildingIndex = which;
                                    if (selectedBuildingIndex > 0) {
                                        mBuildingBtn.setText(
                                                buildingList.get(selectedBuildingIndex - 1).getBuildingCode());
                                    } else {
                                        mBuildingBtn.setText(charSequence);
                                    }
                                    return true;
                                }
                            }).show();
        } else {
            Toast.makeText(this, "No buildings available", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectTags(View view) {
        if (masterTagList != null && masterTagList.size() > 0) {
            new MaterialDialog.Builder(this)
                    .title("Select tags")
                    .items(tagNames)
                    .itemsCallbackMultiChoice(selectedTagPos, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            selectedTagPos = which;
                            String newBtntext = "";
                            for (int selected : which) {
                                newBtntext += "#" + masterTagList.get(selected).getTitle() + ", ";
                            }
                            if (newBtntext.length() > 0) {
                                newBtntext = newBtntext.substring(0, newBtntext.length() - 2);
                            } else {
                                newBtntext = getString(R.string.note_no_tags);
                            }

                            mTagsBtn.setText(newBtntext);

                            return true;
                        }
                    })
                    .positiveText("Choose")
                    .show();
        } else {

        }
    }

    private void initExtras(Bundle b) {
        long noteId = b.getLong(ARG_NOTE_ID, -1);
        if (noteId > 0) {
            DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
            DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
            mNote = dataManager.findById(noteId);
        } else {
            selectedBuildCode = b.getString(ARG_SELECTED_BUILD, "");
            if (selectedBuildCode.isEmpty()) {
                mBuildingBtn.setText(this.getString(R.string.note_no_building));
            } else {
                mBuildingBtn.setText(selectedBuildCode);
            }
            long tagId = b.getLong(ARG_TAG_ID, -1);
            if (tagId > 0) {
                Tag tmp = new Tag();
                tmp.setId(tagId);
                NoteTag noteTag = new NoteTag();
                noteTag.setTag(tmp);
                orgTagList = new ArrayList<>(1);
                orgTagList.add(noteTag);
            }
        }
    }

    private void initTags() {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Tag, Long> dataManager = databaseHelper.getDataManager(Tag.class);

        String tagBtnText = "";

        // initially selected tags
        if (isUpdate) {
            DataManager<NoteTag, Long> dm2 = databaseHelper.getDataManager(NoteTag.class);

            orgTagList = dm2.find(NoteTag.COLUMN_NOTE, mNote);
            Collections.sort(orgTagList, new Comparator<NoteTag>() {
                @Override
                public int compare(NoteTag lhs, NoteTag rhs) {
                    return lhs.getTag().getTitle().compareTo(rhs.getTag().getTitle());
                }
            });

            selectedTagPos = new Integer[orgTagList.size()];

        } else if (orgTagList != null) {
            selectedTagPos = new Integer[orgTagList.size()];

        } else {
            selectedTagPos = new Integer[0];
            orgTagList = new ArrayList();
        }

        // populate full tag list
        masterTagList = dataManager.getAll(Tag.COLUMN_TITLE, true);
        tagNames = new String[masterTagList.size()];
        int selectedIndex = 0;
        Long currentTagId = -1l;
        if (orgTagList.size() > 0) {
            currentTagId = orgTagList.get(0).getTag().getId();
        }

        for (int i = 0; i < masterTagList.size(); i++) {
            Tag tag = masterTagList.get(i);
            tagNames[i] = tag.getTitle();
            if (currentTagId == tag.getId()) {
                tagBtnText += "#" + tag.getTitle() + ", ";
                selectedTagPos[selectedIndex] = i;
                selectedIndex++;
                if (selectedIndex < orgTagList.size()) {
                    currentTagId = orgTagList.get(selectedIndex).getTag().getId();
                } else {
                    currentTagId = -1l;
                }
            }
        }

        if (tagBtnText.length() > 0) {
            tagBtnText = tagBtnText.substring(0, tagBtnText.length()-2);
        } else {
            tagBtnText = getString(R.string.note_no_tags);
        }

        mTagsBtn.setText(tagBtnText);
    }

    private boolean tagListEqual() {
        if (orgTagList.size() != selectedTagPos.length) return false;

        for (int i = 0; i < orgTagList.size(); i++) {
            if (!orgTagList.get(i).getTag().equals(masterTagList.get(selectedTagPos[i]))) return false;
        }

        return true;
    }
}
