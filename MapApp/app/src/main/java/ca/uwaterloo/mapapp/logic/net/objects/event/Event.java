package ca.uwaterloo.mapapp.logic.net.objects.event;

/**
 * Created by Hyunwook on 2015-06-13.
 */
public class Event {

    private double id;
    private String title;
    private String link;
    private Times times;

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

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", times=" + times +
                '}';
    }
}
