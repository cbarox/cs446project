package ca.uwaterloo.mapapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApiJsonDeserializer;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import ca.uwaterloo.mapapp.shared.objects.event.EventTimes;
import ca.uwaterloo.mapapp.ui.adapters.EventNoteAdapter;
import ca.uwaterloo.mapapp.util.ListViewUtil;

public class ViewEventActivity extends ActionBarActivity {
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";
    private static final int REQUEST_IMAGE_CAPTURE = 105;
    @InjectView(R.id.tool_bar)
    protected Toolbar mToolbar;
    @InjectView(R.id.event_title)
    protected TextView title;
    @InjectView(R.id.event_location_txt)
    protected TextView location;
    @InjectView(R.id.event_link_btn)
    protected Button link;
    @InjectView(R.id.event_notes_list)
    protected ListView mNoteList;
    @InjectView(R.id.galleryButton)
    protected Button galleryButton;
    @InjectView(R.id.add_picture)
    protected Button addPictureButton;
    /*@InjectView(R.id.event_more_notes)
    protected Button moreNotes;*/
    @InjectView(R.id.event_sum_total)
    protected TextView sumTotalTextView;
    @InjectView(R.id.event_sum_avg_txt)
    protected TextView sumAvgTextView;
    @InjectView(R.id.event_time_txt)
    protected TextView eventTimeTextView;
    @InjectView(R.id.event_sum_avg_bar)
    protected RatingBar sumAvgRatingBar;
    @InjectView(R.id.event_rating)
    protected RatingBar eventRatingBar;
    @InjectView(R.id.fab_new_note)
    protected FloatingActionButton fab;

    private Event mEvent;
    private List<EventNote> mEventNotes;
    private EventNoteAdapter mAdapter;
    private List<EventTimes> mEventTimes;
    private List<EventRanking> mEventRankings;
    private List<EventImage> mEventImages;
    private String uniqueId;

    private static String byteArrayToHexString(byte[] data) {
        String result = "";
        for (byte b : data) {
            result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private static String hash(String text) {
        byte[] data = text.getBytes();
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA1");
            digester.update(data, 0, data.length);
            byte[] digest = digester.digest();
            return byteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
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
        Integer eventId = b.getInt(ARG_EVENT_ID);
        setEvent(eventId);

        mNoteList.setEmptyView(findViewById(R.id.empty_list_state));
        mNoteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewEventActivity.this, ViewEventNoteActivity.class);
                intent.putExtra(ViewEventNoteActivity.ARG_EVENT_ID, mEvent.getId());
                intent.putExtra(ViewEventNoteActivity.ARG_EVENT_NOTE_ID, mEventNotes.get(position).getId());
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventActivity.this, AddEventNoteActivity.class);
                intent.putExtra(AddEventNoteActivity.ARG_EVENT_ID, mEvent.getId());
                startActivityForResult(intent, AddEventNoteActivity.REQUEST_INSERT);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void setEvent(final Integer eventId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager dataManager = databaseHelper.getDataManager(Event.class);

        mEvent = (Event) dataManager.findFirst(Event.COLUMN_ID, eventId);
        title.setText(Html.fromHtml(mEvent.getTitle()));

        if (mEvent.getLocation() != null && !mEvent.getLocation().isEmpty()) {
            location.setText(mEvent.getLocation());

        } else {
            location.setText("University of Waterloo");
        }

        if (mEvent.getLink() != null && !mEvent.getLink().isEmpty()) {
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = mEvent.getLink();
                    if (!link.startsWith("https://") && !link.startsWith("http://")) {
                        link = "http://" + link;
                    }

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browserIntent);
                }
            });

        } else {
            link.setVisibility(View.INVISIBLE);
        }

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventActivity.this, EventGalleryActivity.class);
                intent.putExtra(EventGalleryActivity.ARG_FILTER_VALUE_INTEGER, mEvent.getId());
                startActivity(intent);
            }
        });
        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        uniqueId = hash(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID) + eventId);

        eventRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                EventRanking eventRanking = new EventRanking();
                eventRanking.setEventId(eventId);
                eventRanking.setId(uniqueId);
                eventRanking.setRanking(rating);
                ICallback callback = new ICallback() {
                    @Override
                    public void call(Object param) {
                        // don't care
                    }
                };
                ServerRestApi.addOrSetEventRanking(callback, eventRanking);
            }
        });

        loadEventNotes();
        loadEventTimes();
        loadEventRankings();
        loadEventImages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) return;

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bitmapStream);
            byte[] b = bitmapStream.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            EventImage eImage = new EventImage();
            eImage.setEventId(mEvent.getId());
            eImage.setBase64(imageEncoded);
            ServerRestApi.addOrSetEventImage(new ICallback() {
                @Override
                public void call(Object param) {
                    Toast.makeText(getApplicationContext(), "Image posted", Toast.LENGTH_SHORT);
                }
            }, eImage);

        } else if (requestCode == AddEventNoteActivity.REQUEST_INSERT) {
            loadEventNotes();
        }
    }

    private void loadEventNotes() {
        final Context context = this;
        ICallback callback = new ICallback() {
            @Override
            public void call(final Object param) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEventNotes = (List<EventNote>) param;
                        mAdapter = new EventNoteAdapter(context, mEventNotes);
                        mNoteList.setAdapter(mAdapter);

                        ListViewUtil.setListViewHeightBasedOnChildren(mNoteList);

                        /*int visibility = mEventNotes.isEmpty() ? View.INVISIBLE : View.VISIBLE;
                        moreNotes.setVisibility(visibility);*/
                    }
                });

            }
        };
        ServerRestApi.requestEventNotes(callback, mEvent.getId());
    }

    private void loadEventTimes() {
        final Context context = this;
        ICallback callback = new ICallback() {
            @Override
            public void call(final Object param) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEventTimes = (List<EventTimes>) param;
                        String times = "";
                        for (EventTimes eventTimes : mEventTimes) {
                            final Calendar start;
                            final Calendar end;
                            try {
                                start = WaterlooApiJsonDeserializer.toCalendar(eventTimes.getStart());
                                String dayName = start.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.CANADA);
                                String month = start.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.CANADA);
                                int day = start.get(Calendar.DAY_OF_MONTH);
                                int startHour = start.get(Calendar.HOUR_OF_DAY);
                                int startMinutes = start.get(Calendar.MINUTE);
                                String startMinutesString = (startMinutes < 10) ? "0" : "";
                                startMinutesString += startMinutes;

                                end = WaterlooApiJsonDeserializer.toCalendar(eventTimes.getEnd());
                                int endHour = end.get(Calendar.HOUR_OF_DAY);
                                int endMinutes = end.get(Calendar.MINUTE);
                                String endMinutesString = (endMinutes < 10) ? "0" : "";
                                endMinutesString += endMinutes;
                                times += dayName + " " + month + " " + day;
                                times += " " + startHour + ":" + startMinutesString;
                                times += " - " + endHour + ":" + endMinutesString + "\n";
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        eventTimeTextView.setText(times.trim());
                    }
                });
            }
        };
        ServerRestApi.requestEventTimes(callback, mEvent.getId());
    }

    private void loadEventRankings() {
        final Context context = this;
        ICallback callback = new ICallback() {
            @Override
            public void call(final Object param) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEventRankings = (List<EventRanking>) param;
                        final int numRankings = mEventRankings.size();
                        sumTotalTextView.setText("" + numRankings);
                        if (numRankings > 0) {
                            float avg = 0;
                            for (EventRanking eventRanking : mEventRankings) {
                                if (eventRanking.getId().equals(uniqueId)) {
                                    eventRatingBar.setRating(eventRanking.getRanking());
                                }
                                avg += eventRanking.getRanking();
                            }
                            avg /= numRankings;
                            sumAvgTextView.setText("" + avg);
                            sumAvgRatingBar.setRating(avg);
                        } else {
                            sumAvgTextView.setText("" + 0);
                            sumAvgRatingBar.setRating(0);
                        }
                    }
                });
            }
        };
        ServerRestApi.requestEventRankings(callback, mEvent.getId());
    }

    private void loadEventImages() {
        final Context context = this;
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                mEventImages = (List<EventImage>) param;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        galleryButton.setText(String.format("%d PHOTOS", mEventImages.size()));
                        if (mEventImages.size() > 0) {
                            EventImage image = mEventImages.get(0);
                            byte[] decodedString = Base64.decode(image.getBase64(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            galleryButton.setBackground(new BitmapDrawable(Resources.getSystem(), decodedByte));
                        }
                    }
                });

                // process/display event rankings here

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mEventImages.isEmpty()) {
                            galleryButton.setVisibility(View.GONE);

                        } else {
                            galleryButton.setVisibility(View.VISIBLE);
                            galleryButton.setText("" + mEventImages.size() + " PHOTOS");
                        }
                    }
                });
            }
        };
        ServerRestApi.requestEventImages(callback, mEvent.getId());
    }
}
