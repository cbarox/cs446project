package ca.uwaterloo.mapapp.data.objects.old;

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
@DatabaseTable(tableName = EventType.TABLE)
public class EventType {

	public static final String TABLE = "event_type";
    public static final String COLUMN_EVENT_TYPE_NAME = "event_type";
	public static final String COLUMN_COLOR_CODE = "color_code";

    @DatabaseField(columnName = COLUMN_EVENT_TYPE_NAME)
    private String event_type_name;
	 
    @DatabaseField(columnName = COLUMN_COLOR_CODE)
    private String color_code;

    public String getEventTypeName () {
        return event_type_name;
    }
    public void setEventTypeName (String event_type_name) {
        this.event_type_name = event_type_name;
    }

    public String getColorCode () {
        return color_code;
    }
    public void setColorCode (String color_code) {
        this.color_code = color_code;
    }
	
}
