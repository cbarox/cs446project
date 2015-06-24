package ca.uwaterloo.mapapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DataManager;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.data.objects.Note;
import ca.uwaterloo.mapapp.logic.Logger;
import ca.uwaterloo.mapapp.logic.net.WaterlooApi;
import ca.uwaterloo.mapapp.logic.net.objects.Building;

public class NewEditNoteActivity extends ActionBarActivity {

    public static final String ARG_SELECTED_BUILD = "selected_building";
    public static final String ARG_NOTE_ID = "note_id";

    public static final int REQUEST_INSERT = 101;
    public static final int REQUEST_UPDATE = 102;

    public static final String RESULT_NOTE_ID = "result_note_id";

    /**
     * All the actions that are processed by the broadcast receiver
     */
    private static final String[] receiverActions = {
            WaterlooApi.ACTION_GOT_LIST
    };

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;

    @InjectView(R.id.note_title)
    protected EditText mTitle;
    @InjectView(R.id.note_desc)
    protected EditText mDescription;
    @InjectView(R.id.btn_building)
    protected Button mBuildingBtn;
    @InjectView(R.id.btn_tags)
    protected Button mTagsBtn;

    private List<Building> buildingList;
    private String[] buildingNames;
    private int selectedIndex = 0;
    private String selectedBuildCode = "";

    private boolean isUpdate = false;
    private Note mNote = null;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            switch (action) {
                case WaterlooApi.ACTION_GOT_LIST: {
                    if (extras == null) {
                        Logger.error("No extras supplied from intent %s", WaterlooApi.ACTION_GOT_LIST);
                        return;
                    }
                    List list = (List) extras.get(WaterlooApi.EXTRA_LIST);
                    if (list == null) {
                        Logger.error("Couldn't get list from intent extras");
                        return;
                    }
                    String className = extras.getString(WaterlooApi.EXTRA_CLASS);
                    if (className == null) {
                        Logger.error("Couldn't get class name from intent extras");
                        return;
                    }
                    if (className.equals("Building")) {
                        handleGotBuildings(list);
                    }
                }
            }
        }
    };

    private void handleGotBuildings(List<Building> buildings) {
        buildingList = buildings;
        buildingNames = new String[buildingList.size()+1];
        buildingNames[0] = "No building";
        int current = 1;
        for (Building building : buildingList) {
            buildingNames[current] = building.getBuildingName();
            if (building.getBuildingCode().equals(selectedBuildCode)) {
                selectedIndex = current;
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

        // Register the broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        for (String action : receiverActions) {
            intentFilter.addAction(action);
        }
        registerReceiver(broadcastReceiver, intentFilter);

        WaterlooApi.requestList(this, Building.class);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            long noteId = b.getLong(ARG_NOTE_ID, -1);
            if (noteId > 0) {
                DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(this);
                DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
                mNote = dataManager.findById(noteId);
            } else {
                selectedBuildCode = b.getString(ARG_SELECTED_BUILD);
                mBuildingBtn.setText(this.getString(R.string.note_no_building));
            }
        }

        if (mNote != null) {
            isUpdate = true;
            selectedBuildCode = mNote.getBuildingCode();
            if (selectedBuildCode != null && !selectedBuildCode.isEmpty()) {
                mBuildingBtn.setText(selectedBuildCode);
            } else {
                mBuildingBtn.setText(this.getString(R.string.note_no_building));
            }

            mTitle.setText(mNote.getTitle());
            mDescription.setText(mNote.getDescription());
        } else {
            // give focus to title text
            if (mTitle.requestFocus()) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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
        final Note org = mNote.copy();

        mNote.setTitle(title);
        if (title.isEmpty() && !description.isEmpty()) {
            if (description.length() > 15) {
                mNote.setTitle(description.substring(0, 16) + "...");
            } else {
                mNote.setTitle(description);
            }
        }
        mNote.setDescription(description);

        if (selectedIndex > 0) {
            mNote.setBuildingCode(mBuildingBtn.getText().toString());
        } else {
            mNote.setBuildingCode("");
        }

        // confirm update of existing note
        if (confirmUpdate && !org.equals(mNote)) {
            // TODO: add check if note is empty
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
        } else if (org.equals(mNote)){
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
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(this);
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
        dataManager.insertOrUpdate(mNote);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }



    public void selectNewBuilding(View view) {
        if (buildingList != null && buildingList.size() > 0) {
            new MaterialDialog.Builder(this)
                    .title("Choose building")
                    .items(buildingNames)
                    .itemsCallbackSingleChoice(selectedIndex,
                            new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, View view, int which,
                                                   CharSequence charSequence) {
                            selectedIndex = which;
                            if (selectedIndex > 0) {
                                mBuildingBtn.setText(
                                        buildingList.get(selectedIndex-1).getBuildingCode());
                            } else {
                                mBuildingBtn.setText(charSequence);
                            }
                            return true;
                        }
                    }).show();
        }
    }
}
