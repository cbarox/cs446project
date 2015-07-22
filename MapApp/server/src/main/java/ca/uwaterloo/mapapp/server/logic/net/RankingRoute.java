package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.Gson;

import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.shared.net.ServerResponse;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class RankingRoute implements IGetSetDeleteRoute {
    private static Gson gson = new Gson();

    @Override
    public Object get(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        DataManager<EventRanking, String> rankingsDataManager = databaseHelper.getDataManager(EventRanking.class);
        EventRanking ranking = rankingsDataManager.findFirst(EventRanking.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));
        
        return gson.toJson(ranking)); 
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        EventRanking ranking = gson.fromJson(request.body(), EventRanking.class);
        DataManager<EventRanking, String> rankingsDataManager = databaseHelper.getDataManager(EventRanking.class);
        if( rankingsDataManager.insertOrUpdate(ranking) == null )
            return "fail";
        
        return "success";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        final DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        
        EventRanking ranking = new EventRanking();
        ranking.setId(Integer.parseInt(request.params(":id")));
        
        DataManager<EventRanking, String> rankingsDataManager = databaseHelper.getDataManager(EventRanking.class);
        if( !rankingsDataManager.delete(ranking) )
            return "fail";
            
        return "success";
    }
}
