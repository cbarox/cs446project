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
@DatabaseTable(tableName = DayOfWeek.TABLE)
public class DayOfWeek {

	public static final String TABLE = "day_of_week";
    public static final String COLUMN_DAY = "day_of_week";
	public static final String COLUMN_EVENT_ID = "event_id";

    @DatabaseField(columnName = COLUMN_DAY)
    private String column_day;
	 
    @DatabaseField(columnName = COLUMN_EVENT_ID)
    private Long event_id;

    public String getColumnDay () {
        return column_day;
    }
    public void setColumnDay (String column_day) {
        this.column_day = column_day;
    }

    public Long getEventId () {
        return event_id;
    }
    public void setEventId (Long event_id) {
        this.event_id = event_id;
    }
	
}
