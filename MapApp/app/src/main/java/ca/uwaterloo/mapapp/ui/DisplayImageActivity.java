package ca.uwaterloo.mapapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.ui.adapters.EventImageAdapter;

public class DisplayImageActivity extends ActionBarActivity {
    public static final String ARG_IMAGE = "ARG_IMAGE";

    @InjectView(R.id.eventImage)
    protected ImageView eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        ButterKnife.inject(this);

        Bundle b = getIntent().getExtras();
        EventImage image = (EventImage)b.getSerializable(ARG_IMAGE);

        byte[] decodedString = Base64.decode(image.getBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        eventImage.setImageBitmap(decodedByte);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return false; }
}
