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
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RANKING = "ranking";
    public static final String COLUMN_EVENT_ID = "eventId";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private Long id;

    @DatabaseField(columnName = COLUMN_RANKING)
    private Integer ranking;

    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Integer eventId;
    
    public Integer getEventId() {
        return eventId;
    }
    
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
    
    public Integer getRanking() {
        return ranking;
    }
    
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EventRanking{" +
                "id=" + id +
                ", ranking=" + ranking +
                ", eventId=" + eventId +
                '}';
    }
}
