package ca.uwaterloo.mapapp.server;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;

import ca.uwaterloo.mapapp.server.logic.net.BuildingDataUpdater;
import ca.uwaterloo.mapapp.server.logic.net.EventDataUpdater;
import ca.uwaterloo.mapapp.server.logic.net.IGetSetDeleteRoute;
import ca.uwaterloo.mapapp.server.logic.net.ImageRoute;
import ca.uwaterloo.mapapp.server.logic.net.NoteRoute;
import ca.uwaterloo.mapapp.server.logic.net.RankingRoute;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.ServerResponse;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.post;

public class Main {

    public static final String DATABASE_URL = "jdbc:mysql://localhost/whatsnuw";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "whatsnuw";
    private static final Timer DATA_UPDATE_TIMER = new Timer("dataUpdateTimer");
    private static final int PERIOD_ONE_DAY = 86400000;
    private static final int PERIOD_THREE_WEEKS = 1814400000;
    private static HashMap<Class, DataManager> dataManagers = new HashMap<>();
    private static ConnectionSource connectionSource;
    private static BuildingDataUpdater buildingDataUpdater = new BuildingDataUpdater();
    private static EventDataUpdater eventDataUpdater = new EventDataUpdater();

    public static void main(String[] args) {
        // Initialize the database
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            TableUtils.createTableIfNotExists(connectionSource, EventNote.class);
            TableUtils.createTableIfNotExists(connectionSource, EventRanking.class);
            //TableUtils.createTableIfNotExists(connectionSource, EventImage.class);
            TableUtils.createTableIfNotExists(connectionSource, Building.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
        } catch (SQLException e) {
            System.err.println("Unable to create connection to the database");
            e.printStackTrace();
            return;
        }

        // Update the data once in a while
        DATA_UPDATE_TIMER.scheduleAtFixedRate(buildingDataUpdater, 0, PERIOD_THREE_WEEKS);
        DATA_UPDATE_TIMER.scheduleAtFixedRate(eventDataUpdater, 0, PERIOD_ONE_DAY);

        postGetSetDelete("note", new NoteRoute());
        postGetSetDelete("image", new ImageRoute());
        postGetSetDelete("ranking", new RankingRoute());
    }

    private static void postGetSetDelete(String type, final IGetSetDeleteRoute route)
    {
        get(String.format("/%s/get/:event", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.get(request, response);
            }
        });
        post(String.format("/%s/set", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.set(request, response);
            }
        });
        post(String.format("/%s/delete/:id", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.delete(request, response);
            }
        });
    }

    public static DataManager getDataManager(Class clazz) {
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
