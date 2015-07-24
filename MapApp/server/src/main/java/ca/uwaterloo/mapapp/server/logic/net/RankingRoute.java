package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.Gson;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class RankingRoute implements IGetSetDeleteRoute {
    private static Gson gson = new Gson();

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
        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        if( rankingsDataManager.insertOrUpdate(ranking) == null ) {
            response.status(500);
            return "Failed to insert object";
        }

        System.out.println(ranking.toString());

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
            return "Failed to delete object";
        }
            
        response.status(200);
        return "";
    }
}
