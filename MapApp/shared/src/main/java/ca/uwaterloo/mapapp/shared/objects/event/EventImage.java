package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 7/6/15
 * <p>
 * A single image that is associated with a particular event
 */
@DatabaseTable(tableName = EventImage.TABLE)
public class EventImage implements Serializable {
    public static final String TABLE = "event_images";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BASE64 = "base64";
    public static final String COLUMN_EVENT_ID = "event_id";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private Long id;

    @DatabaseField(columnName = COLUMN_BASE64)
    private String base64;

    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Integer eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EventImage{" +
                "id=" + id +
                ", base64='" + base64.length() + " byte string\'" +
                ", eventId=" + eventId +
                '}';
    }
}
