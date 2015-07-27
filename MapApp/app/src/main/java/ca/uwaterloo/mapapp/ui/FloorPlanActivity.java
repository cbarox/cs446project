package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.logic.net.FloorplanApi;
import ca.uwaterloo.mapapp.objects.floorplan.FloorPlanDatabase;
import ca.uwaterloo.mapapp.objects.floorplan.Room;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.ui.adapters.FloorSelectorAdapter;

public class FloorPlanActivity extends ActionBarActivity {

    public static final String ARG_BUILDING_CODE = "arg_building_code";

    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;
    @InjectView(R.id.prog_bar)
    protected ProgressBar progressBar;
    @InjectView(R.id.floor_map)
    protected SubsamplingScaleImageView mFloorMap;
    @InjectView(R.id.floor_selector)
    protected ListView mFloorSelector;

    private boolean isOptionsVisible = true;
    private String mBuildingCode = "";

    private FloorPlanDatabase floorPlan;
    private List<Room> mRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
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

        mFloorMap.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }
        });

        mBuildingCode = getIntent().getExtras().getString(ARG_BUILDING_CODE);
        final GestureDetector gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mFloorMap.isReady()) {
                    PointF sCoord = mFloorMap.viewToSourceCoord(e.getX(), e.getY());
                    if (!mapClick(sCoord))
                        toggleOptionsVisible();
                }
                return true;
            }
        });
        mFloorMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

        mFloorSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadFloor(position);
            }
        });

        ICallback floorPlanCallback = new ICallback() {
            @Override
            public void call(Object param) {
                List<FloorPlanDatabase> buildings = (List<FloorPlanDatabase>) param;
                for (FloorPlanDatabase db : buildings) {
                    if (db.getName().startsWith(mBuildingCode)) {
                        floorPlan = db;
                        break;
                    }
                }

                loadFloorPlans();
            }
        };

        FloorplanApi.requestFloorplanList(floorPlanCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void loadFloorPlans() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFloorSelector.setAdapter(new FloorSelectorAdapter(FloorPlanActivity.this,
                        floorPlan.getFloors()));
            }
        });
        loadFloor(0);
    }

    private void loadFloor(int floorNum) {
        progressBar.setVisibility(View.VISIBLE);

        String png = floorPlan.getFloors().get(floorNum).getPng();

        FloorplanApi.requestFloorplanImage(png, this, loadImageCallback);
        FloorplanApi.requestRoomList(loadRoomsCallback, png.substring(0, png.length()-4));
    }

    private void toggleOptionsVisible() {
        int visibility;
        if (isOptionsVisible)
            visibility = View.INVISIBLE;
        else
            visibility = View.VISIBLE;

        mFloorSelector.setVisibility(visibility);
        mToolbar.setVisibility(visibility);
        isOptionsVisible = !isOptionsVisible;
    }

    private ICallback loadImageCallback = new ICallback() {
        @Override
        public void call(Object param) {
            final String path = (String)param;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFloorMap.setImage(ImageSource.uri(path));
                }
            });
        }
    };

    private ICallback loadRoomsCallback = new ICallback() {
        @Override
        public void call(Object param) {
            mRooms = (List<Room>)param;
        }
    };

    private boolean mapClick(PointF coord) {
        for (Room room : mRooms) {
            List<Integer> mid = room.getMid();
            if (inRange(mid.get(0), mid.get(1), coord)) {
                Intent intent = new Intent(this, FilteredNoteListActivity.class);
                intent.putExtra(FilteredNoteListActivity.ARG_FILTER_TYPE, FilteredNoteListActivity.FILTER_BUILDING);
                intent.putExtra(FilteredNoteListActivity.ARG_FILTER_BUILDING_CODE, mBuildingCode);
                intent.putExtra(FilteredNoteListActivity.ARG_FILTER_ROOM_NUMBER, room.getNumber());
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    private boolean inRange(int x, int y, PointF coord) {
        int bx = (int)coord.x;
        int by = (int)coord.y;

        int d = (int)Math.sqrt(Math.pow(bx - 2*x, 2) + Math.pow(by - 2*y, 2));

        return d < 150;
    }
}
