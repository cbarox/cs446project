package ca.uwaterloo.mapapp.shared.objects.event;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 6/30/15
 * <p/>
 * This object is used by the database on both the server and the app and can be private or public
 */
@DatabaseTable(tableName = EventNote.TABLE)
public class EventNote implements Serializable {
    public static final String TABLE = "eventnotes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENT_ID = "eventId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_LAST_MODIFIED = "last_modified";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private Long id;

    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Integer eventId;
    
    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_DATE_CREATED)
    private String dateCreated;

    @DatabaseField(columnName = COLUMN_LAST_MODIFIED)
    private String lastModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "EventNote{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}
