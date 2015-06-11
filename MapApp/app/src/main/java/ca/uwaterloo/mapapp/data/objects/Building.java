package ca.uwaterloo.mapapp.data.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 23/05/15
 * <p/>
 * This class is a Java Bean that acts as a database object.
 * Each field represents a column in the table and the class represents a table in the database,
 * where each row is an instance of this class.
 */
@DatabaseTable(tableName = Building.TABLE)
public class Building implements Serializable {

    public static final String TABLE = "building";

    public static final String COLUMN_BUILDING_ID = "building_id";
    public static final String COLUMN_BUILDING_CODE = "building_code";
    public static final String COLUMN_BUILDING_NAME = "building_name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    @DatabaseField(columnName = COLUMN_BUILDING_ID, id = true)
    private String buildingId;

    @DatabaseField(columnName = COLUMN_BUILDING_CODE)
    private String buildingCode;

    @DatabaseField(columnName = COLUMN_BUILDING_NAME)
    private String buildingName;

    @DatabaseField(columnName = COLUMN_LATITUDE)
    private double latitude;

    @DatabaseField(columnName = COLUMN_LONGITUDE)
    private double longitude;

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
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
                "buildingId='" + buildingId + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", buildingName='" + buildingName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
