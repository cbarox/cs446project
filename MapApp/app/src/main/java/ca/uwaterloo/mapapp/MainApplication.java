package ca.uwaterloo.mapapp;

import android.app.Application;

import ca.uwaterloo.mapapp.data.DatabaseHelper;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public class MainApplication extends Application {

    public static final boolean DEBUG = false;

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
                exception.printStackTrace();
            }
        });
    }
}
