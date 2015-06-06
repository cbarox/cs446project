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
@DatabaseTable(tableName = Note.TABLE)
public class Note {

	public static final String TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_DATE_CREATED = "date_created";
	public static final String COLUMN_DATE_MOTIFIED = "date_modified";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_ROOM_ID = "room_id";
	
    @DatabaseField(columnName = COLUMN_ID, generatedId = true  )
    private long id;

    @DatabaseField(columnName = COLUMN_USERNAME)
    private String username;

    @DatabaseField(columnName = COLUMN_DATE_CREATED)
    private long date_created;

    @DatabaseField(columnName = COLUMN_DATE_MOTIFIED)
    private long date_motified;

    @DatabaseField(columnName = COLUMN_CONTENT)
    private String date_content;

    @DatabaseField(columnName = COLUMN_ROOM_ID)
    private long room_id;
	
    public long getId () {
        return id;
    }
    public void setId (long id) {
        this.id = id;
    }
	
	public String getUsername () {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
	
	public long getDateCreated () {
        return date_created;
    }
    public void setDateCreated(long date_created) {
        this.date_created = date_created;
    }

	public long getDateModified () {
        return date_motified;
    }
    public void setDateModified(long date_motified) {
        this.date_created = date_motified;
    }

		
    public long getRoomId () {
        return room_id;
    }
    public void setRoomId (long room_id) {
        this.room_id = room_id;
    }
	
}
