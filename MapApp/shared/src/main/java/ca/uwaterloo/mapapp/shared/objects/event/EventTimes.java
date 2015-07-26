package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 6/13/15
 */
@DatabaseTable(tableName = EventTimes.TABLE)
public class EventTimes implements Serializable {

    public static final String TABLE = "times";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_EVENT_ID = "event_id";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = COLUMN_START)
    private String start;

    @DatabaseField(columnName = COLUMN_END)
    private String end;

    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Integer eventId;

    public long getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
