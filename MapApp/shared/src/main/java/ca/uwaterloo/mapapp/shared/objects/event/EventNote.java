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
    public static final String COLUMN_EVENT_ID = "event_id";
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
}
