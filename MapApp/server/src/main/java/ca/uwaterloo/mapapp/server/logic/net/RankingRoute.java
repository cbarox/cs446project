package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class RankingRoute implements IGetSetDeleteRoute {
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Override
    public Object get(Request request, Response response) throws Exception {
        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        EventRanking ranking = rankingsDataManager.findFirst(EventRanking.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));

        response.status(200);
        return gson.toJson(ranking);
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventRanking ranking = gson.fromJson(request.body(), EventRanking.class);
        System.out.println(ranking.toString());

        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        if( rankingsDataManager.insertOrUpdate(ranking) == null ) {
            response.status(500);
            System.err.println("Failed to insert or update ranking for event " + ranking.getEventId() + ". Database error.");
            return "Failed to insert object";
        }

        response.status(200);
        return "";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventRanking ranking = new EventRanking();
        ranking.setId(Long.parseLong(request.params(":id")));
        
        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        if( !rankingsDataManager.delete(ranking) ) {
            response.status(500);
            System.err.println("Failed to delete ranking with id " + ranking.getId() + ". Database error.");
            return "Failed to delete object";
        }
            
        response.status(200);
        return "";
    }
}
