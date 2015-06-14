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
import android.widget.EditText;
import android.widget.Toast;

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
        String message = "Cannot save empty note";

        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

        if (!title.isEmpty()) {
            Note note = new Note();
            note.setTitle(title);
            note.setDescription(description);
            // TODO get building code from dropdown
            //note.setBuildingCode(buildingCode);

            // Insert note into database
            DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(this);
            DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
            dataManager.insert(note);

            message = "Note saved";
        }

        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
