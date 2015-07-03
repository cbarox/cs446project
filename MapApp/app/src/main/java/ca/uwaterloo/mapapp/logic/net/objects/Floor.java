package ca.uwaterloo.mapapp.logic.net.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by brwarner2 on 01/07/2015.
 */
@DatabaseTable(tableName = Floor.TABLE)
public class Floor implements Serializable {
    public static final String TABLE = "floor";

    public static final String COLUMN_PDF = "pdf";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PNG = "png";

    @DatabaseField(columnName=COLUMN_PDF)
    private String pdf;

    @DatabaseField(columnName=COLUMN_NAME)
    private String name;

    @DatabaseField(columnName=COLUMN_PNG)
    private String png;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "pdf='" + pdf + '\'' +
                ", name='" + name + '\'' +
                ", png='" + png + '\'' +
                '}';
    }
}
