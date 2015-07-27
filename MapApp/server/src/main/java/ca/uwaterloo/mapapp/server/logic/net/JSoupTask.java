package ca.uwaterloo.mapapp.server.logic.net;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ca.uwaterloo.mapapp.shared.ICallback;

/**
 * Created by Hyunwook on 2015-06-14.
 */
public class JSoupTask implements Runnable {

    private String url;
    private String selector;
    private ICallback callback;

    /**
     * @param url      The url to get the data from
     * @param selector The selector to
     * @param callback The callback that is called when the parsing is done
     */
    public JSoupTask(String url, String selector, ICallback callback) {
        this.url = url;
        this.selector = selector;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            if (url == null) {
                Log.i("Jsoup run()", "URL is null" );
                callback.call(null);
            }
            Document doc = Jsoup.connect(url).get();
            Element element = doc.select(selector/*e.g. span.fn*/).first();
            if (element != null) {
                callback.call(element.toString());
            } else {
                Log.i("Jsoup run()", "Element is null" );
                callback.call(null);
            }
        } catch (Exception e) {
            Log.e("Jsoup run()", "Exception: ", e );
            e.printStackTrace();
            callback.call(null);
        }
    }
}


