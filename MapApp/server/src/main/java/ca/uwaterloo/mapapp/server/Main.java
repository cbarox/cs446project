package ca.uwaterloo.mapapp.server;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import ca.uwaterloo.mapapp.server.logic.net.BuildingDataUpdater;
import ca.uwaterloo.mapapp.server.logic.net.EventDataUpdater;
import ca.uwaterloo.mapapp.server.logic.net.IGetSetDeleteRoute;
import ca.uwaterloo.mapapp.server.logic.net.ImageRoute;
import ca.uwaterloo.mapapp.server.logic.net.NoteRoute;
import ca.uwaterloo.mapapp.server.logic.net.RankingRoute;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.delete;
import static spark.Spark.get;
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
    private static BuildingDataUpdater buildingDataUpdater;
    private static EventDataUpdater eventDataUpdater;

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });

        // Initialize the database
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            TableUtils.createTableIfNotExists(connectionSource, EventNote.class);
            TableUtils.createTableIfNotExists(connectionSource, EventRanking.class);
            //TableUtils.createTableIfNotExists(connectionSource, EventImage.class);
            TableUtils.createTableIfNotExists(connectionSource, Building.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
            buildingDataUpdater = new BuildingDataUpdater();
            eventDataUpdater = new EventDataUpdater();
        } catch (SQLException e) {
            System.err.println("Unable to create connection to the database");
            e.printStackTrace();
            return;
        }

        // Update the data once in a while
        DATA_UPDATE_TIMER.scheduleAtFixedRate(buildingDataUpdater, 0, PERIOD_THREE_WEEKS);
        DATA_UPDATE_TIMER.scheduleAtFixedRate(eventDataUpdater, 10000, PERIOD_ONE_DAY);

        postGetSetDelete("note", new NoteRoute());
        postGetSetDelete("image", new ImageRoute());
        postGetSetDelete("ranking", new RankingRoute());
        get("/events", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                final DataManager dataManager = getDataManager(Event.class);
                System.out.println("Getting events from database");
                final List events = dataManager.getAll();
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                System.out.println("Converting to JSON");
                response.body(gson.toJson(events));
                System.out.println("Sending JSON");
                return gson.toJson(events);
            }
        });
    }

    private static void postGetSetDelete(final String type, final IGetSetDeleteRoute route)
    {
        get(String.format("/%s/:event", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    return route.get(request, response);
                }
                catch(Exception e)
                {
                    System.err.println("Failed to get '" + type + "'. Exception: " + e.getMessage());
                }
                return null;
            }
        });
        post(String.format("/%s", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    return route.set(request, response);
                }
                catch(Exception e)
                {
                    System.err.println("Failed to set '" + type + "'. Exception: " + e.getMessage());
                }
                return null;
            }
        });
        delete(String.format("/%s/:id", type), new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                try {
                    return route.delete(request, response);
                }
                catch(Exception e)
                {
                    System.err.println("Failed to delete '" + type + "'. Exception: " + e.getMessage());
                }
                return null;
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
