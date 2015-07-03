package ca.uwaterloo.mapapp.server.logic.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Hyunwook on 2015-06-14.
 */
public class JsoupApi implements Runnable {

    private String url;
    private String selector;
    private Callback callback;

    /**
     * @param url      The url to get the data from
     * @param selector The selector to
     * @param callback
     */
    public JsoupApi(String url, String selector, Callback callback) {
        this.url = url;
        this.selector = selector;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(url).get();
            Element element = doc.select(selector/*e.g. span.fn*/).first();
            callback.call(element.toString());
        } catch (Exception e) {
            callback.call(null);
        }
    }
}


