package ca.uwaterloo.mapapp.shared.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.IRequestor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cjbarrac
 * 6/11/15
 */
public class WaterlooApi {
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(List.class, new WaterlooApiJsonDeserializer<List>())
            .create();
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(IWaterlooApiRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    private static IWaterlooApiRestService service = restAdapter.create(IWaterlooApiRestService.class);

    private static HashMap<String, Object> localCacheMap = new HashMap<>();

    public static void requestBuildings(final ICallback callback) {
        requestData("Building", callback, new IRequestor() {
            @Override
            public Object request() {
                return service.getBuildings();
            }
        });
    }

    public static void requestEvents(final ICallback callback) {
        requestData("Event", callback, new IRequestor() {
            @Override
            public Object request() {
                return service.getEvents();
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
                    System.err.println( "WaterlooApi: Error in requestor thread" + e.toString() );
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
