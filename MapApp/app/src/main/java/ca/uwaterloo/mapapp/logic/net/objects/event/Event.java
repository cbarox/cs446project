package ca.uwaterloo.mapapp.logic.net.objects.event;

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
    public static final String COLUMN_TIMES = "times";
    public static final String COLUMN_TITLE = "title";

    @DatabaseField(columnName = COLUMN_ID)
    private int id;

    @DatabaseField(columnName = COLUMN_LINK)
    private String link;

    @DatabaseField(columnName = COLUMN_TIMES)
    private Times times;

    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", times=" + times +
                ", title='" + title + '\'' +
                '}';
    }
}
