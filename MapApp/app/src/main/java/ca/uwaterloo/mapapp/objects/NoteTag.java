package ca.uwaterloo.mapapp.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kev on 2015-07-06.
 */
@DatabaseTable(tableName = NoteTag.TABLE)
public class NoteTag {

    public static final String TABLE = "noteTag";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TAG = "tag";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = COLUMN_NOTE)
    private Note note;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = COLUMN_TAG)
    private Tag tag;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public Note getNote() { return note; }

    public void setNote(Note note) { this.note = note; }

    public Tag getTag() { return tag; }

    public void setTag(Tag tag) { this.tag = tag; }

    @Override
    public String toString() {
        return "NoteTag{" +
                "note='" + note.toString() + '\'' +
                ", tag='" + tag.toString() + '\'' +
                "}";
    }
}
