package ca.uwaterloo.mapapp.logic;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;

import ca.uwaterloo.mapapp.data.DataManager;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.data.objects.Building;
import ca.uwaterloo.mapapp.logic.net.NetworkHelper;

/**
 * Created by cjbarrac
 * 6/6/15
 */
public class Buildings {

    public static final String ACTION_BUILDINGS_PROCESSED = "ACTION_BUILDINGS_PROCESSED";

    public static void updateBuildings(final Context context) {
        FutureCallback<JsonObject> futureCallback = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                processBuildings(context, result);
                Intent buildingsIntent = new Intent(ACTION_BUILDINGS_PROCESSED);
                context.sendBroadcast(buildingsIntent);
            }
        };
        NetworkHelper.getJsonWithKey(context, "buildings/list.json", futureCallback);
    }

    private static void processBuildings(Context context, JsonObject result) {
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(context);
        DataManager<Building, String> buildingDataManager = databaseHelper.getDataManager(Building.class);

        JsonArray dataArray = result.getAsJsonArray("data");
        ArrayList<Building> buildings = new ArrayList<>();
        for (int buildingIndex = 0; buildingIndex < dataArray.size(); buildingIndex++) {
            JsonObject buildingObject = (JsonObject) dataArray.get(buildingIndex);

            JsonElement jsonElement = buildingObject.get("building_code");
            if (jsonElement.isJsonNull()) {
                continue;
            }
            String code = jsonElement.getAsString();

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

            Building building = new Building();
            building.setCode(code);
            building.setName(name);
            building.setLatitude(latitude);
            building.setLongitude(longitude);

            buildings.add(building);
        }

        buildingDataManager.insertOrUpdateAll(buildings);
    }

}
