package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.mapapp.logic.Logger;
import ca.uwaterloo.mapapp.logic.net.objects.FloorPlanDatabase;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by brwarner
 * 6/11/15
 */
public class FloorplanApi {
    public static final String INTENT_PREFIX = "ca.uwaterloo.mapapp.logic.net.WaterlooApi.";
    public static final String ACTION_GOT_FLOORS = INTENT_PREFIX + "ACTION_GOT_FLOORS";
    public static final String ACTION_GOT_ROOMS = INTENT_PREFIX + "ACTION_GOT_ROOMS";
    public static final String EXTRA_LIST = INTENT_PREFIX + "EXTRA_LIST";
    public static final String EXTRA_CLASS = INTENT_PREFIX + "EXTRA_CLASS";

    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(List.class, new FloorplanApiJsonDeserializer<List>())
            .create();
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(FloorplanApiRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    private static FloorplanApiRestService service = restAdapter.create(FloorplanApiRestService.class);

    private static HashMap<String, List> localCacheMap = new HashMap<>();

    public static void requestFloorplanList(final Context context) {
        final String key = "floorplans";
        if (localCacheMap.containsKey(key)) {
            broadcastGotList(context, localCacheMap.get(key), ACTION_GOT_FLOORS);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                try {
                    final List result = service.getFloorPlanDatabases();
                    localCacheMap.put(key, result);
                    final long duration = System.currentTimeMillis() - startTime;
                    Logger.info("Got floor plan list from database in %dms", duration);
                    Logger.info("Found %d buildings", result.size());
                    broadcastGotList(context, result, ACTION_GOT_FLOORS);
                } catch (Exception e) {
                    Logger.error("Failed to get floor plan list from database", e);
                }

            }
        }).start();
    }

    public static void requestRoomList(final Context context, final String floor) {
        final String key = String.format("rooms%s", floor);
        if (localCacheMap.containsKey(key)) {
            broadcastGotList(context, localCacheMap.get(key), ACTION_GOT_ROOMS);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                try {
                    final List result = service.getRooms(floor);
                    localCacheMap.put(key, result);
                    final long duration = System.currentTimeMillis() - startTime;
                    Logger.info("Got room list for %s from database in %dms", floor, duration);
                    Logger.info("Found %d rooms", result.size());
                    broadcastGotList(context, result, ACTION_GOT_ROOMS);
                } catch (Exception e) {
                    Logger.error("Failed to get room list from database for floor %s", e, floor);
                }

            }
        }).start();
    }

    private static void broadcastGotList(Context context, List list, final String intent) {
        Intent buildingsIntent = new Intent(intent);
        buildingsIntent.putExtra(EXTRA_LIST, new ArrayList<>(list));
        context.sendBroadcast(buildingsIntent);
    }

}
