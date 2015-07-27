package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.uwaterloo.mapapp.logic.net.serialization.FloorplanApiJsonDeserializer;
import ca.uwaterloo.mapapp.logic.net.services.IFloorPlanRestService;
import ca.uwaterloo.mapapp.objects.floorplan.FloorPlanDatabase;
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

    private static final String FloorplanURI = "http://104.236.77.229:8000/png/";

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
                    Log.i("FloorplanApi", "requestData complete" );
                    callback.call(result);
                } catch (Exception e) {
                    Log.e("FloorplanApi", "Exception:" , e);
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static void requestFloorplanImage(final String floor, final Context context, final ICallback callback) {
        try {
            final URL floorplanURL = new URL(FloorplanURI + floor + ".png");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String absoluteDownloadPath = DownloadFile(floorplanURL, floor, context);
                        Log.i("Floorplan", "Downloaded floorplan image to " + absoluteDownloadPath);
                        callback.call(absoluteDownloadPath);
                    } catch (Exception e) {
                        Log.e("FloorplanApi", "Exception:" , e);
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e("FloorplanApi", "Exception:" , e);
            e.printStackTrace();
        }
    }

    private static String DownloadFile(URL floorplanURL, String saveAsFilename, Context context) throws IOException {
        // Get where the file should be downloaded to the cache
        File cacheDir = getCacheFolder(context);
        File cacheFile = new File(cacheDir, saveAsFilename + ".png");

        // If it is already there, just return
        if(cacheFile.exists())
            return cacheFile.getAbsolutePath();

        // Open an http stream source and a file stream destination
        URLConnection connection = floorplanURL.openConnection();
        InputStream inputStream = new BufferedInputStream(floorplanURL.openStream(), 10240);
        FileOutputStream outputStream = new FileOutputStream(cacheFile);

        // Copy the file
        byte buffer[] = new byte[1024];
        int dataSize = 0;
        while((dataSize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, dataSize);
        }

        // Close streams
        inputStream.close();
        outputStream.close();
        return cacheFile.getAbsolutePath();
    }

    private static File getCacheFolder(Context context)
    {
        File cacheDir = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "whatsnuwcache");
            if(!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
        }

        if(!cacheDir.isDirectory()) {
            cacheDir = context.getCacheDir();
        }

        return cacheDir;
    }
}
