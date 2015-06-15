package ca.uwaterloo.mapapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
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

public class NewNoteActivity extends ActionBarActivity {

    public static final String ARG_SELECTED_BUILD = "selected_building";

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
        setContentView(R.layout.activity_new_note);
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
            selectedBuildCode = b.getString(ARG_SELECTED_BUILD);
            mBuildingBtn.setText(selectedBuildCode);
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


    @Override
    public void onBackPressed() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        if (!title.isEmpty()) {
            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            // TODO get building code from dropdown
            if (selectedIndex > 0) {
                note.setBuildingCode(mBuildingBtn.getText().toString());
            }

            // Insert note into database
            DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(this);
            DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
            dataManager.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
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
