package ca.uwaterloo.mapapp;

import android.app.Application;

import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.logic.Logger;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the static database helper instance
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        DatabaseHelper.setDatabaseHelper(databaseHelper);

        // Make sure the app doesn't crash, just log failures
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable exception) {
                Logger.error("Caught an unhandled exception", exception);
            }
        });
    }
}
