package ca.uwaterloo.mapapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ca.uwaterloo.mapapp.data.objects.Building;
import ca.uwaterloo.mapapp.data.objects.Event;
import ca.uwaterloo.mapapp.data.objects.EventType;
import ca.uwaterloo.mapapp.data.objects.Floor;
import ca.uwaterloo.mapapp.data.objects.Note;
import ca.uwaterloo.mapapp.data.objects.Room;
import ca.uwaterloo.mapapp.data.objects.Organization;
import ca.uwaterloo.mapapp.data.objects.DayOfWeek;

/**
 * Created by cjbarrac
 * 23/05/15
 * <p>
 * This class basically controls all the accesses to the database, and handles the creation and
 * upgrading of the database.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "whatsnuw.db";

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Building.class);
            TableUtils.createTable(connectionSource, Room.class);
            TableUtils.createTable(connectionSource, Event.class);
			TableUtils.createTable(connectionSource, EventType.class);
			TableUtils.createTable(connectionSource, Floor.class);
			TableUtils.createTable(connectionSource, Note.class);
			TableUtils.createTable(connectionSource, Organization.class);
			TableUtils.createTable(connectionSource, DayOfWeek.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // This doesn't do anything because there is no older versions
    }
}
