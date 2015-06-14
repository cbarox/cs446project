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
@DatabaseTable(tableName = Organization.TABLE)
public class Organization {

	public static final String TABLE = "organization";
    public static final String COLUMN_ORGANIZATION = "organization";
	public static final String COLUMN_TYPE = "type";

    @DatabaseField(columnName = COLUMN_ORGANIZATION)
    private String organization;
	 
    @DatabaseField(columnName = COLUMN_TYPE)
    private String type;

    public String getOrganization () {
        return organization;
    }
    public void setOrganization (String organization) {
        this.organization = organization;
    }

    public String getType () {
        return type;
    }
    public void setType (String Type) {
        this.type = type;
    }
	
}
