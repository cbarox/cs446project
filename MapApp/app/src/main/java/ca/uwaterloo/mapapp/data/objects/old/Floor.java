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
@DatabaseTable(tableName = Floor.TABLE)
public class Floor {

	public static final String TABLE = "floor";
    public static final String COLUMN_FLOOR_NUMBER = "floor_num";
    public static final String COLUMN_FLOOR_MAP_FILE = "floor_map_file";
    public static final String COLUMN_BUILDING_ID = "building_id";
	
    @DatabaseField(columnName = COLUMN_FLOOR_NUMBER )
    private Long floor_num;

    @DatabaseField(columnName = COLUMN_FLOOR_MAP_FILE)
    private String floor_map_file;

    @DatabaseField(columnName = COLUMN_BUILDING_ID)
    private long building_id;
	
    public Long getFloorNum() {
        return floor_num;
    }
	
    public void setFloorNum(Long floor_num) {
        this.floor_num = floor_num;
    }

    public String getFloorMapFile() {
        return floor_map_file;
    }

    public void setFloorMapFile(String floor_map_file) {
        this.floor_map_file = floor_map_file;
    }
		
    public long getBuildingId() {
        return building_id;
    }

    public void setBuildingId(long building_id) {
        this.building_id = building_id;
    }
}
