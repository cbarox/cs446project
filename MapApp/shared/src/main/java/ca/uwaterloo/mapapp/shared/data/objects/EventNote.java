package ca.uwaterloo.mapapp.shared.data.objects;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by cjbarrac
 * 6/30/15
 */
@DatabaseTable(tableName = EventNote.TABLE)
public class EventNote {
    public static final String TABLE = "eventnotes";
    public static final String COLUMN_ID = "id";
}
