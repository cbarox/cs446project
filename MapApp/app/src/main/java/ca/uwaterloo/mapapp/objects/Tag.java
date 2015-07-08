package ca.uwaterloo.mapapp.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kev on 2015-07-06.
 */
@DatabaseTable(tableName = Tag.TABLE)
public class Tag {
    public static final String TABLE = "tag";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private long id;

    @DatabaseField(columnName = COLUMN_TITLE)
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != Tag.class) return false;
        Tag other = (Tag)o;

        if (id != other.id) return false;
        if (title == null && other.title != null) return false;
        if (title == null && other.title == null) return true;
        if (!title.equals(other.title)) return false;

        return title.equals(other.title);
    }
}
