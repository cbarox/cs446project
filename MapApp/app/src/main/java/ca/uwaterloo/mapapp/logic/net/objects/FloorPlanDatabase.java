package ca.uwaterloo.mapapp.logic.net.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by brwarner2 on 01/07/2015.
 */
@DatabaseTable(tableName = FloorPlanDatabase.TABLE)
public class FloorPlanDatabase implements Serializable {
    public static final String TABLE = "floorplans";

    public static final String COLUMN_FLOORS = "floors";
    public static final String COLUMN_NAME = "name";

    @DatabaseField(columnName = COLUMN_FLOORS)
    private List<Floor> floors;

    @DatabaseField(columnName = COLUMN_NAME)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    @Override
    public String toString() {
        return "FloorPlanDatabase{" +
                "floors=" + floors +
                ", name='" + name + '\'' +
                '}';
    }
}
