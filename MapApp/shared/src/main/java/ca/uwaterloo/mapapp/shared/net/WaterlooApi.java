package ca.uwaterloo.mapapp.shared.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.mapapp.shared.ICallback;
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

    private static HashMap<String, List> localCacheMap = new HashMap<>();

    public static void requestList(final ICallback callback, final Class clazz) {
        final String className = clazz.getSimpleName();
        if (localCacheMap.containsKey(className)) {
            callback.call(localCacheMap.get(className));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List result = invokeApiMethodFromClassName(className);
                    localCacheMap.put(className, result);
                    callback.call(result);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

            }
        }).start();
    }

    private static List invokeApiMethodFromClassName(String className) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String format = String.format("get%ss", className);
        Method method = service.getClass().getMethod(format);
        return (List) method.invoke(service);
    }

}
