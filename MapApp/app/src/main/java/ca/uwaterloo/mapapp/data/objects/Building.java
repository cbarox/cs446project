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
@DatabaseTable(tableName = Building.TABLE)
public class Building {

    public static final String TABLE = "building";

    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FLOORS = "floors";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    @DatabaseField(columnName = COLUMN_CODE, id = true)
    private String code;

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = COLUMN_FLOORS)
    private int floors;

    @DatabaseField(columnName = COLUMN_LATITUDE)
    private double latitude;

    @DatabaseField(columnName = COLUMN_LONGITUDE)
    private double longitude;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Building{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", floors=" + floors +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
