package ca.uwaterloo.mapapp.logic.net;

import android.content.Context;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ca.uwaterloo.mapapp.logic.Logger;

/**
 * Created by Hyunwook on 2015-06-14.
 */
public class JsoupApi implements Runnable {

    public static final String INTENT_PREFIX = "ca.uwaterloo.mapapp.logic.net.JsoupApi.";
    public static final String ACTION_GOT_RESULT = INTENT_PREFIX + "ACTION_GOT_RESULT";
    public static final String EXTRA_RESULT = INTENT_PREFIX + "EXTRA_RESULT";
    public static final String EXTRA_CLASS = INTENT_PREFIX + "EXTRA_CLASS";
    public static final String EXTRA_ID = INTENT_PREFIX + "EXTRA_ID";

    private String url;      // url to get information from
    private String selector; // selects text using special keyword
    private static Class clazz;
    private Context context;
    private double id;       // object id

    /* Returns selected element for the class */
    public void setJsoupApi(final Context context, String url, String selector,
                         Class clazz, double id ) {
        this.selector = selector;
        this.url = url;
        this.clazz = clazz;
        this.id = id;
        this.context = context;
    }

    public void run() {
        try {
            Element element = null;
            Document doc = Jsoup.connect(url).get();
            element = doc.select(selector/*e.g. span.fn*/).first();
            Logger.info("Jsoup Result OK (" + url + ") : " + element.toString());
            broadcastGotResult(id, context, element.toString());
        } catch (Exception e) {
            Logger.error("Jsoup Result BAD(" + url + ") Request Failed" );
        }
    }

    private static void broadcastGotResult(double id, Context context, String result) {
        try {
            Intent JsoupIntent = new Intent(ACTION_GOT_RESULT);
            JsoupIntent.putExtra(EXTRA_ID, id);
            JsoupIntent.putExtra(EXTRA_CLASS, clazz.getSimpleName());
            JsoupIntent.putExtra(EXTRA_RESULT, result);
            /* TODO BUG context == NULL here  context is defined at AllEventsFragment.handleGotEvents*/
            context.sendBroadcast(JsoupIntent);
        } catch (Exception e) {
            Logger.error("Broadcast (Jsoup) Failed" );
        }
    }
}


