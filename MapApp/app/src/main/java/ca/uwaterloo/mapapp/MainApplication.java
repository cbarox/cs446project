package ca.uwaterloo.mapapp;

import android.app.Application;
import android.util.Log;

import ca.uwaterloo.mapapp.data.DatabaseHelper;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public class MainApplication extends Application {

    public static final boolean DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper.setDatabaseHelper(new DatabaseHelper(this));

        if (DEBUG) {
            return;
        }

        // Make sure the app doesn't crash, just log failures
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable exception) {
                String msg = String.format("Caught unhandled exception\n%s", exception.toString());
                Log.e("Whats nUW", msg);
                exception.printStackTrace();
            }
        });
    }
}
