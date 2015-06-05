package ca.uwaterloo.mapapp.data.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cjbarrac
 * 23/05/15
 * <p/>
 * This class is a Java Bean that acts as a database object.
 * Each field represents a column in the table and the class represents a table in the database,
 * where each row is an instance of this class.
 */
@DatabaseTable(tableName = Event.TABLE)
public class Event {

    public static final String TABLE = "event";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_END_TIME = "end_time";
	public static final String COLUMN_TERM = "term";
	public static final String COLUMN_ORGANIZATION  = "organization";
	public static final String COLUMN_EVENT_TYPE  = "event_type";
	
    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;


    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_START_TIME)
    private long start_time;

    @DatabaseField(columnName = COLUMN_END_TIME)
    private long end_time;

    @DatabaseField(columnName = COLUMN_TERM)
    private long term;

    @DatabaseField(columnName = COLUMN_ORGANIZATION)
    private String organization;
	
    @DatabaseField(columnName = COLUMN_EVENT_TYPE)
    private String event_type;
	
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
	public long getStartTime () {
        return start_time;
    }
    public void setStartTime (long start_time) {
        this.start_time = start_time;
    }

	public long getEndTime () {
        return end_time;
    }
    public void setEndTime (long end_time) {
        this.end_time = end_time;
    }

	public long getTerm () {
        return term;
    }
    public void setTerm (long term) {
        this.term = term;
    }	
	
	public String getOrganization () {
        return organization;
    }
    public void setOrganization (String term) {
        this.organization = organization;
    }	

	public String getEventType () {
        return event_type;
    }
    public void setEventType (String event_type) {
        this.event_type = event_type;
    }	
}
