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
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.IRequestor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by brwarner
 * 6/11/15
 */
public class FloorplanApi {
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(List.class, new FloorplanApiJsonDeserializer<List>())
            .create();
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(IFloorPlanRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    private static IFloorPlanRestService service = restAdapter.create(IFloorPlanRestService.class);

    private static HashMap<String, Object> localCacheMap = new HashMap<>();

    public static void requestFloorplanList(final ICallback callback) {
        requestData("Floorplans", callback, new IRequestor() {
            @Override
            public Object request() {
                return service.getFloorPlanDatabases();
            }
        });
    }

    public static void requestRoomList(final ICallback callback, final String floor) {
        requestData(String.format("Room%s", floor), callback, new IRequestor() {
            @Override
            public Object request() {
                return service.getRooms(floor);
            }
        });
    }

    private static void requestData(final String cacheKey, final ICallback callback, final IRequestor requestor)
    {
        if(localCacheMap.containsKey(cacheKey)) {
            callback.call(localCacheMap.get(cacheKey));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Object result = requestor.request();
                    localCacheMap.put(cacheKey, result);
                    callback.call(result);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

            }
        }).start();
    }
}
