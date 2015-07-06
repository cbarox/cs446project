package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.mapapp.logic.net.serialization.FloorplanApiJsonDeserializer;
import ca.uwaterloo.mapapp.logic.net.services.IFloorPlanRestService;
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
            .setEndpoint(IFloorPlanRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    private static IFloorPlanRestService service = restAdapter.create(IFloorPlanRestService.class);

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
                try {
                    final List result = service.getFloorPlanDatabases();
                    localCacheMap.put(key, result);
                    broadcastGotList(context, result, ACTION_GOT_FLOORS);
                } catch (Exception e) {
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
                try {
                    final List result = service.getRooms(floor);
                    localCacheMap.put(key, result);
                    broadcastGotList(context, result, ACTION_GOT_ROOMS);
                } catch (Exception e) {
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
