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
@DatabaseTable(tableName = Room.TABLE)
public class Room {

    public static final String TABLE = "room";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_FLOOR_NUMBER = "floor_num";
    
    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = COLUMN_NUMBER)
    private long number;

    @DatabaseField(columnName = COLUMN_LOCATION)
    private String location;

    @DatabaseField(columnName = COLUMN_FLOOR_NUMBER)
    private long floor_num;
	
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber () {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
		
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
		
    public long getFloorNum() {
        return floor_num;
    }
	
    public void setFloorNum(long floor_num) {
        this.floor_num = floor_num;
    }
}
