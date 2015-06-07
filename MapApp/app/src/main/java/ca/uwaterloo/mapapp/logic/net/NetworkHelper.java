package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public class NetworkHelper {

    public static final String API_KEY = "22e67ab71cbd805f4f2dbe9d89fd7286";
    public static final String URL_PREFIX = "https://api.uwaterloo.ca/v2/";

    public static void getJson(Context context, String uriSuffix, FutureCallback<JsonObject> callback) {
        String uri = String.format("%s%s", URL_PREFIX, uriSuffix);
        Ion.with(context)
                .load(uri)
                .asJsonObject()
                .setCallback(callback);
    }

    public static void getJsonWithKey(Context context, String uriSuffix, FutureCallback<JsonObject> callback) {
        String uri = String.format("%s?key=%s", uriSuffix, API_KEY);
        getJson(context, uri, callback);
    }
}
