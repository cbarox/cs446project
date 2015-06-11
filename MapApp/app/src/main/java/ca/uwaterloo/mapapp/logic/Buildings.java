package ca.uwaterloo.mapapp.logic;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapapp.data.DataManager;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.data.objects.Building;
import ca.uwaterloo.mapapp.logic.net.NetworkHelper;

/**
 * Created by cjbarrac
 * 6/6/15
 */
public class Buildings {

    public static final String ACTION_BUILDINGS_PROCESSED = "ca.uwaterloo.mapapp.logic.Buildings.ACTION_BUILDINGS_PROCESSED";
    public static final String EXTRA_BUILDINGS = "ca.uwaterloo.mapapp.logic.Buildings.EXTRA_BUILDINGS";

    public static void updateBuildings(final Context context) {
        List<Building> result;
        try {
            long startTime = System.currentTimeMillis();
            result = NetworkHelper.service.getBuildings();
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("Got buildings from API in %dms", duration);
        } catch (Exception e) {
            Logger.error("Could not get buildings from API", e);
            result = null;
        }
        List<Building> buildings = processObjects(context, result, Building.class);
        broadcastBuildingsProcessed(context, buildings);
    }

    private static void broadcastBuildingsProcessed(Context context, List<Building> buildings) {
        Intent buildingsIntent = new Intent(ACTION_BUILDINGS_PROCESSED);
        buildingsIntent.putExtra(EXTRA_BUILDINGS, new ArrayList<>(buildings));
        context.sendBroadcast(buildingsIntent);
    }

    /**
     * Processes all the objects retrieved from the API into the database
     *
     * @param context A context for the database helper
     * @param result  The list of objects returned by the API call
     * @return The list of objects returned from the API
     */
    private static <T> List<T> processObjects(Context context, List<T> result, Class clazz) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(context);
        DataManager<T, String> dataManager = databaseHelper.getDataManager(clazz);

        // If we can't access the waterloo API just use the database cached version
        List<T> dbObjects = dataManager.getAll();
        if (result == null) {
            return dbObjects;
        }

        dataManager.passiveInsertAll(result);
        return result;
    }

}
