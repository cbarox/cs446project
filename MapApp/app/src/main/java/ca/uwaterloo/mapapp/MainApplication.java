package ca.uwaterloo.mapapp;

import android.app.Application;

import java.util.ArrayList;

import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.data.objects.Building;
import ca.uwaterloo.mapapp.logic.Buildings;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public class MainApplication extends Application {

    public static ArrayList<Building> buildings = new ArrayList<>();
    private static DatabaseHelper databaseHelper;

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Buildings.updateBuildings(this);
        databaseHelper = new DatabaseHelper(this);
    }
}
