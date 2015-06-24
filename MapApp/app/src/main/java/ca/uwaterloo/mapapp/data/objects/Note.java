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
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_BUILDING_CODE = "building_code";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_BUILDING_CODE)
    private String buildingCode;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) { this.buildingCode = buildingCode; }

    public Note copy() {
        Note newNote = new Note();
        newNote.id = this.id;
        newNote.title = this.title;
        newNote.description = this.description;
        newNote.buildingCode = this.buildingCode;
        return newNote;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != Note.class) return false;

        Note other = (Note)o;

        if (other.buildingCode == null && buildingCode != null) return false;
        if (other.title == null && title != null) return false;
        if (other.description == null && description != null) return false;

        if (id != other.id) return false;
        if (other.buildingCode == null || !other.buildingCode.equals(buildingCode)) return false;
        if (other.title == null        || !other.title.equals(title)) return false;
        if (other.description == null  || !other.description.equals(description)) return false;
        return true;
    }
}
