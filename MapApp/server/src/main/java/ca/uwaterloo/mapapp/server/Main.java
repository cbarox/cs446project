package ca.uwaterloo.mapapp.server;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;

import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.ServerResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.post;

public class Main {

    public static final String DATABASE_URL = "jdbc:mysql://localhost/whatsnuw";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "whatsnuw";
    private static Gson gson = new Gson();
    private static HashMap<Class, DataManager> dataManagers = new HashMap<>();
    private static ConnectionSource connectionSource;

    public static void main(String[] args) {
        // Initialize the database
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            //TableUtils.createTableIfNotExists(connectionSource, Note.class);
        } catch (SQLException e) {
            System.err.println("Unable to create connection to the database");
            e.printStackTrace();
            return;
        }

        // insert or update a note
        post("/eventnote", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                ServerResponse serverResponse = new ServerResponse();
                serverResponse.setStatus("success");
                serverResponse.setId(0L);
                return serverResponse;
            }
        });
    }

    private static DataManager getDataManager(Class clazz) {
        if (dataManagers.containsKey(clazz)) {
            return dataManagers.get(clazz);
        }
        try {
            Dao dao = DaoManager.createDao(connectionSource, clazz);
            DataManager dataManager = new DataManager(dao);
            dataManagers.put(clazz, dataManager);
            return dataManager;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
