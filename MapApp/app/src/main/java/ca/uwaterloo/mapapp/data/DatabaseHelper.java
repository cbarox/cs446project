package ca.uwaterloo.mapapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import ca.uwaterloo.mapapp.objects.NoteTag;
import ca.uwaterloo.mapapp.objects.Tag;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;

/**
 * Created by cjbarrac
 * 23/05/15
 * <p/>
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
     * Get an instance of the database helper
     *
     * @return A static instance of DatabaseHelper
     */
    public static DatabaseHelper getDatabaseHelper() {
        return instance;
    }

    /**
     * Set the static instance of database helper to be used in the entire app
     * Initialized in MainApplication after application context is created in onCreate
     *
     * @param databaseHelper An instance of DatabaseHelper
     */
    public static void setDatabaseHelper(DatabaseHelper databaseHelper) {
        instance = databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Building.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
            TableUtils.createTableIfNotExists(connectionSource, Note.class);
            TableUtils.createTable(connectionSource, Tag.class);
            TableUtils.createTable(connectionSource, NoteTag.class);
        } catch (SQLException e) {
            Log.e("Whats nUW", "Error creating tables:\n" + e.toString());
        }
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
