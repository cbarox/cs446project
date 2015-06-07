package ca.uwaterloo.mapapp.logic;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

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
        final long start = System.currentTimeMillis();
        // TODO Move this processing off the main thread and into an Android service
        FutureCallback<JsonObject> futureCallback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    Logger.error("Couldn't get buildings from API");
                    return;
                }
                long duration = System.currentTimeMillis() - start;
                Logger.debug("Retrieving buildings from API took %sms", duration);
                List<Building> buildings = processBuildings(context, result);
                broadcastBuildingsProcessed(buildings, context);
                duration = System.currentTimeMillis() - start;
                Logger.debug("Updating buildings took %sms total", duration);
            }
        };

        NetworkHelper.getJsonWithKey(context, "buildings/list.json", futureCallback);
    }

    private static void broadcastBuildingsProcessed(List<Building> buildings, Context context) {
        Intent buildingsIntent = new Intent(ACTION_BUILDINGS_PROCESSED);
        buildingsIntent.putExtra(EXTRA_BUILDINGS, new ArrayList<>(buildings));
        context.sendBroadcast(buildingsIntent);
    }

    /**
     * Processes all the buildings retrieved from the API into the database
     *
     * @param context A context for the database helper
     * @param result  The JSON object returned by the API call
     * @return The list of buildings returned from the API
     */
    private static List<Building> processBuildings(Context context, JsonObject result) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(context);
        DataManager<Building, String> buildingDataManager = databaseHelper.getDataManager(Building.class);
        List<Building> existingBuildings = buildingDataManager.getAll();
        ArrayList<String> existingBuildingCodes = new ArrayList<>(existingBuildings.size());
        for (Building building : existingBuildings) {
            existingBuildingCodes.add(building.getCode());
        }

        JsonArray dataArray = result.getAsJsonArray("data");
        ArrayList<Building> buildingsToInsert = new ArrayList<>();
        for (int buildingIndex = 0; buildingIndex < dataArray.size(); buildingIndex++) {
            JsonObject buildingObject = (JsonObject) dataArray.get(buildingIndex);

            JsonElement jsonElement = buildingObject.get("building_code");
            if (jsonElement.isJsonNull()) {
                continue;
            }
            String code = jsonElement.getAsString();

            // Skip if it's already in the database
            if (existingBuildingCodes.contains(code)) {
                continue;
            }

            jsonElement = buildingObject.get("building_name");
            if (jsonElement.isJsonNull()) {
                continue;
            }
            String name = jsonElement.getAsString();

            jsonElement = buildingObject.get("latitude");
            if (jsonElement.isJsonNull()) {
                continue;
            }
            double latitude = jsonElement.getAsDouble();

            jsonElement = buildingObject.get("longitude");
            if (jsonElement.isJsonNull()) {
                continue;
            }
            double longitude = jsonElement.getAsDouble();

            Building newBuilding = new Building();
            newBuilding.setCode(code);
            newBuilding.setName(name);
            newBuilding.setLatitude(latitude);
            newBuilding.setLongitude(longitude);
            buildingsToInsert.add(newBuilding);
        }

        buildingDataManager.passiveInsertAll(buildingsToInsert);
        existingBuildings.addAll(buildingsToInsert);

        return existingBuildings;
    }

}
