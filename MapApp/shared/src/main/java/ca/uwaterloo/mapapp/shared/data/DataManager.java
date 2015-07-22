package ca.uwaterloo.mapapp.shared.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by cjbarrac
 * 6/7/15
 */
public class DataManager<T, ID> {

    private Dao<T, ID> dao;

    public DataManager(Dao<T, ID> dao) {
        this.dao = dao;
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertOrUpdateAll(final List<T> objects) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.createOrUpdate(object);
                    }
                    return null;
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * Delete object from the database
     *
     * @param object the object to delete
     */
    public boolean delete(T object) {
        try {
            dao.delete(object);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Delete all objects from the database
     * @param objects
     * @return
     */
    public boolean deleteAll(final List<T> objects) {
        try {
            dao.delete(objects);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Insert object into the database
     *
     * @param object the object to insert
     * @return The new database ID of the object that was inserted, or null if insert failed
     */
    public ID insert(T object) {
        try {
            dao.create(object);
            return dao.extractId(object);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Insert or update object in the database
     *
     * @param object the object to insert or update
     * @return The database ID of the object that was inserted/updated, or null if insert/update failed
     */
    public ID insertOrUpdate(T object) {
        try {
            dao.createOrUpdate(object);
            return dao.extractId(object);
        } catch (SQLException e) {
            return null;
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
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertAll(final T[] objects) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.create(object);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Inserts multiple objects into the database quickly by committing them all at once
     *
     * @param objects The objects to insert
     */
    public void insertAll(final List<T> objects) {
        try {
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (T object : objects) {
                        dao.create(object);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Find object by Id
     *
     * @param id the id of the object to find
     */
    public T findById(ID id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
        }
        return null;
    }

    public List<T> find(String columnName, Object value) {
        List<T> objects = new ArrayList<>();
        try {
            objects = dao.queryForEq(columnName, value);
        } catch (SQLException e) {
        }
        return objects;
    }

    public List<T> find(String columnName, Object value, String sortColumn, boolean isAsc) {
        List<T> objects = null;
        try {
            QueryBuilder<T, ID> qb = dao.queryBuilder();
            qb.setWhere(qb.where().eq(columnName, value));
            qb.orderBy(sortColumn, isAsc);
            objects = qb.query();
        } catch (SQLException e) {}
        return objects;
    }

    public T findFirst(String columnName, Object value) throws NullPointerException {
        List<T> objects = new ArrayList<>();
        try {
            objects = dao.queryForEq(columnName, value);
        } catch (SQLException e) {
        }
        if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    public T findFirst(String[] columnNames, Object[] values) throws NullPointerException {
        List<T> objects = new ArrayList<>();
        try {
            QueryBuilder<T, ID> qb = dao.queryBuilder();
            Where<T, ID> where = qb.where();
            for (int i = 0; i < columnNames.length; i++) {
                where.eq(columnNames[i], values[i]);
                if (i != columnNames.length-1) {
                    where.and();
                }
            }
            qb.setWhere(where);
            objects = qb.query();
        } catch (SQLException e) {}
        if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    /**
     * Retrieve all objects
     *
     * @return A list of objects from the database or null if the operation failed
     */
    public List<T> getAll() {
        List<T> objects = null;
        try {
            objects = dao.queryForAll();
        } catch (SQLException e) {
        }
        return objects;
    }

    public List<T> getAll(String sortColumn, boolean isAsc) {
        List<T> objects = null;
        try {
            QueryBuilder<T, ID> qb = dao.queryBuilder();
            qb.orderBy(sortColumn, isAsc);
            objects = qb.query();
        } catch (SQLException e) {}
        return objects;
    }

    public void update(T object) {
        try {
            dao.update(object);
        } catch (SQLException e) {
        }
    }

    protected Dao<T, ID> getDao() {
        return this.dao;
    }

}
