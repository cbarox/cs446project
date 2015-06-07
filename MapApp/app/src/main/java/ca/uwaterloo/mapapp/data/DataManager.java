package ca.uwaterloo.mapapp.data;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ca.uwaterloo.mapapp.logic.Logger;

/**
 * Created by cjbarrac
 * 6/7/15
 */
public class DataManager<T, ID> {

    private Dao<T, ID> dao;
    private String className;

    public DataManager(Dao<T, ID> dao) {
        this.dao = dao;
        this.className = dao.getDataClass().getSimpleName();
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertOrUpdateAll(final List<T> objects) {
        try {
            long startTime = System.currentTimeMillis();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.createOrUpdate(object);
                    }
                    return null;
                }
            });
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss inserted/updated in database in %dms", objects.size(), className, duration);
        } catch (Exception e) {
            Logger.error("Error inserting new %ss in the database", e, className);
        }
    }

    /**
     * Delete object from the database 
     *
     * @param object the object to delete
     */
    public void delete(T object) {
        try {
            long startTime = System.currentTimeMillis();
            dao.delete(object);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%s deleted in database in %dms", className, duration);
        } catch (SQLException e) {
            Logger.error("Error deleting %s from the database", e, className);
        }
    }

    /**
     * Insert object into the database 
     *
     * @param object the object to insert
     */
    public void insert(T object) {
        try {
            long startTime = System.currentTimeMillis();
            dao.create(object);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%s inserted into database in %dms", className, duration);
        } catch (SQLException e) {
            Logger.error("Error inserting new %s in the database", e, className);
        }
    }

    /**
     * Insert or update object in the database 
     *
     * @param object the object to insert or update
     */
    public void insertOrUpdate(T object) {
        try {
            long startTime = System.currentTimeMillis();
            dao.createOrUpdate(object);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%s inserted/updated in database in %dms", className, duration);
        } catch (SQLException e) {
            Logger.error("Error inserting/updating %s in the database", e, className);
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     * If an object already exists, it just skips them
     *
     * @param objects The objects to insert
     */
    public void passiveInsertAll(final T[] objects) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.createIfNotExists(object);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            Logger.error("Error passively inserting %ss in the database", e, className);
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     * If an object already exists, it just skips them
     *
     * @param objects The objects to insert
     */
    public void passiveInsertAll(final List<T> objects) {
        try {
            long startTime = System.currentTimeMillis();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.createIfNotExists(object);
                    }
                    return null;
                }
            });
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss passively inserted into to database in %dms", objects.size(), className, duration);
        } catch (Exception e) {
            Logger.error("Error passively inserting %ss in the database", e, className);
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertAll(final T[] objects) {
        try {
            long startTime = System.currentTimeMillis();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.create(object);
                    }
                    return null;
                }
            });
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss inserted into database in %dms", objects.length, className, duration);
        } catch (Exception e) {
            Logger.error("Error inserting new %ss in the database", e, className);
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertAll(final List<T> objects) {
        try {
            long startTime = System.currentTimeMillis();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.create(object);
                    }
                    return null;
                }
            });
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss inserted into database in %dms", objects.size(), className, duration);
        } catch (Exception e) {
            Logger.error("Error inserting new %ss into the database", e, className);
        }
    }

    /**
     * Find object by Id 
     *
     * @param id the id of the object to find
     */
    public T findById(ID id) {
        try {
            long startTime = System.currentTimeMillis();
            T object = dao.queryForId(id);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%s found in database in %dms", className, duration);
            return object;
        } catch (SQLException e) {
            Logger.error("Error finding %s by id in the database", e, className);
        }
        return null;
    }

    public List<T> find(String columnName, Object value) {
        List<T> objects = new ArrayList<>();
        try {
            long startTime = System.currentTimeMillis();
            objects = dao.queryForEq(columnName, value);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss found in database in %dms", objects.size(), className, duration);
        } catch (SQLException e) {
            Logger.error("Error finding %s in the database", e, className);
        }
        return objects;
    }

    public T findFirst(String columnName, Object value) throws NullPointerException {
        List<T> objects = new ArrayList<>();
        try {
            long startTime = System.currentTimeMillis();
            objects = dao.queryForEq(columnName, value);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss found in database in %dms", objects.size(), className, duration);
        } catch (SQLException e) {
            Logger.error("Error finding %s in the database", e, className);
        }
        if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    /**
     * Retrive all objects
     *
     */
    public List<T> getAll() {
        List<T> objects = null;
        try {
            long startTime = System.currentTimeMillis();
            objects = dao.queryForAll();
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%d %ss retrieved from database in %dms", objects.size(), className, duration);
        } catch (SQLException e) {
            Logger.error("Error getting all %ss from the database", e, className);
        }
        return objects;
    }

    public void update(T object) {
        try {
            long startTime = System.currentTimeMillis();
            dao.update(object);
            long duration = System.currentTimeMillis() - startTime;
            Logger.info("%s updated in database in %dms", className, duration);
        } catch (SQLException e) {
            Logger.error("Error updating %s in the database", e, className);
        }
    }

    protected Dao<T, ID> getDao() {
        return this.dao;
    }

}
