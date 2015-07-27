package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by brwarner
 * 7/6/15
 * <p/>
 * A user submitted ranking for an event
 */
@DatabaseTable(tableName = EventRanking.TABLE)
public class EventRanking implements Serializable {
    public static final String TABLE = "event_rankings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RANKING = "ranking";
    public static final String COLUMN_EVENT_ID = "event_id";

    @DatabaseField(columnName = COLUMN_RANKING)
    private Float ranking;

    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Integer eventId;

    @DatabaseField(columnName = COLUMN_ID)
    private String id;

    public Float getRanking() {
        return ranking;
    }

    public void setRanking(Float ranking) {
        this.ranking = ranking;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EventRanking{" +
                "ranking=" + ranking +
                ", eventId=" + eventId +
                ", id='" + id + '\'' +
                '}';
    }
}
