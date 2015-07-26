package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Hyunwook on 2015-06-13.
 */
@DatabaseTable(tableName = Event.TABLE)
public class Event implements Serializable {

    public static final String TABLE = "event";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_TITLE = "title";

    /**
     * Not generated by database, retrieved from API
     */
    @DatabaseField(columnName = COLUMN_ID, id = true)
    private Integer id;

    @DatabaseField(columnName = COLUMN_LINK)
    private String link;

    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    @DatabaseField(columnName = COLUMN_LOCATION)
    private String location;

    private EventTimes[] times;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EventTimes[] getTimes() {
        return times;
    }

    public void setTimes(EventTimes[] times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                "}";
    }
}
