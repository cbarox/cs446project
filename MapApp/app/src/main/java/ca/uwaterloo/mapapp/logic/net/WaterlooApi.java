package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.mapapp.logic.Logger;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by cjbarrac
 * 6/11/15
 */
public class WaterlooApi {
    public static final String INTENT_PREFIX = "ca.uwaterloo.mapapp.logic.net.WaterlooApi.";
    public static final String ACTION_GOT_LIST = INTENT_PREFIX + "ACTION_GOT_LIST";
    public static final String EXTRA_LIST = INTENT_PREFIX + "EXTRA_LIST";
    public static final String EXTRA_CLASS = INTENT_PREFIX + "EXTRA_CLASS";

    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(List.class, new WaterlooApiJsonDeserializer<List>())
            .create();
    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(WaterlooApiRestService.API_ENDPOINT)
            .setConverter(new GsonConverter(gson))
            .build();
    private static WaterlooApiRestService service = restAdapter.create(WaterlooApiRestService.class);

    public static void requestList(final Context context, final Class clazz) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long startTime = System.currentTimeMillis();
                final String className = clazz.getSimpleName();
                try {
                    final List result = invokeApiMethodFromClassName(className);
                    final long duration = System.currentTimeMillis() - startTime;
                    Logger.info("Got list of %ss from API in %dms", className, duration);
                    broadcastGotList(context, result, clazz);
                } catch (Exception e) {
                    Logger.error("Failed to get list of %ss from API", e, className);
                }

            }
        }).start();
    }

    private static List invokeApiMethodFromClassName(String clazzSimpleName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String format = String.format("get%ss", clazzSimpleName);
        Method method = service.getClass().getMethod(format);
        return (List) method.invoke(service);
    }

    private static void broadcastGotList(Context context, List list, Class clazz) {
        Intent buildingsIntent = new Intent(ACTION_GOT_LIST);
        buildingsIntent.putExtra(EXTRA_CLASS, clazz.getSimpleName());
        buildingsIntent.putExtra(EXTRA_LIST, new ArrayList<>(list));
        context.sendBroadcast(buildingsIntent);
    }

}
