package ca.uwaterloo.mapapp.logic.net.objects.event;

import java.io.Serializable;

/**
 * Created by Hyunwook on 2015-06-13.
 */
public class Event implements Serializable {

    private double id;
    private String title;
    private String link;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
