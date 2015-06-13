package ca.uwaterloo.mapapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;

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

    private static DatabaseHelper instance;
    private static HashMap<Class, DataManager> dataManagers = new HashMap<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Lazily load the database helper
     *
     * @param context An application context
     * @return A static instance of DatabaseHelper
     */
    public static DatabaseHelper getDatabaseHelper(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        /*try {
            TableUtils.createTable(connectionSource, Room.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, EventType.class);
            TableUtils.createTable(connectionSource, Floor.class);
            TableUtils.createTable(connectionSource, Note.class);
            TableUtils.createTable(connectionSource, Organization.class);
            TableUtils.createTable(connectionSource, DayOfWeek.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // This doesn't do anything because there is no older versions
    }

    /**
     * Lazy load a data manager
     *
     * @return A static instance of a DataManager
     */
    public <T, ID> DataManager<T, ID> getDataManager(Class clazz) {
        DataManager dataManager = dataManagers.get(clazz);
        if (dataManager != null) {
            return dataManager;
        }
        Dao<T, ID> dao;
        try {
            dao = getDao(clazz);
            dataManager = new DataManager<>(dao);
            dataManagers.put(clazz, dataManager);
            return dataManager;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
