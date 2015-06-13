package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapapp.logic.Logger;
import ca.uwaterloo.mapapp.logic.net.objects.Building;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cjbarrac
 * 6/11/15
 */
public class WaterlooApi {
    public static final String ACTION_GOT_BUILDINGS = "ca.uwaterloo.mapapp.logic.net.WaterlooApi.ACTION_GOT_BUILDINGS";
    public static final String EXTRA_BUILDINGS = "ca.uwaterloo.mapapp.logic.net.WaterlooApi.EXTRA_BUILDINGS";
    public static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Building.class, new WaterlooApiJsonDeserializer<Building>())
            .registerTypeAdapter(List.class, new WaterlooApiJsonDeserializer<List>())
            .create();
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(WaterlooApiRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    public static WaterlooApiRestService service = restAdapter.create(WaterlooApiRestService.class);

    public static void requestBuildings(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                List<Building> result = null;
                try {
                    result = service.getBuildings();
                } catch (Exception e) {
                    Logger.error("Failed to get Buildings from API", e);
                }
                long duration = System.currentTimeMillis() - startTime;
                Logger.info("Got Buildings from API in %dms", duration);
                broadcastBuildingsProcessed(context, result);
            }
        }).start();
    }

    private static void broadcastBuildingsProcessed(Context context, List<Building> buildings) {
        Intent buildingsIntent = new Intent(ACTION_GOT_BUILDINGS);
        buildingsIntent.putExtra(EXTRA_BUILDINGS, new ArrayList<>(buildings));
        context.sendBroadcast(buildingsIntent);
    }

}
