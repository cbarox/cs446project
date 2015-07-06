package ca.uwaterloo.mapapp.objects.floorplan;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by brwarner2 on 03/07/2015.
 */
@DatabaseTable(tableName = Room.TABLE)
public class Room implements Serializable {
    public static final String TABLE = "room";

    public static final String COLUMN_MID = "mid";
    public static final String COLUMN_NUMBER = "number";

    @DatabaseField(columnName=COLUMN_MID)
    private List<Integer> mid;

    @DatabaseField(columnName=COLUMN_NUMBER)
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Integer> getMid() {
        return mid;
    }

    public void setMid(List<Integer> mid) {
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "Room{" +
                "mid=" + mid +
                ", number='" + number + '\'' +
                '}';
    }
}
