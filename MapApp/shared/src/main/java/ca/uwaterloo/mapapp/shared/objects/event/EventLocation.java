package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 7/6/15
 * This is populated by JSoup from the event website and is stored on the server
 */
@DatabaseTable(tableName = EventLocation.TABLE)
public class EventLocation implements Serializable {
    public static final String TABLE = "event_location";

    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_LOCATION = "location";

    @DatabaseField(columnName = COLUMN_EVENT_ID, id = true)
    private Integer eventId;

    @DatabaseField(columnName = COLUMN_LOCATION)
    private String location;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
