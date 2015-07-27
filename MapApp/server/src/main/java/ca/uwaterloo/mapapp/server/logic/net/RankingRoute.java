package ca.uwaterloo.mapapp.server.logic.net;
import android.util.Log;
import java.util.List;

import ca.uwaterloo.mapapp.server.MagicLogger;
import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class RankingRoute implements IGetSetDeleteRoute {

    @Override
    public Object get(Request request, Response response) throws Exception {
        final String eventIdString = request.params(":event");
        if (eventIdString != null) {
            final int eventId;
            try {
                eventId = Integer.parseInt(eventIdString);
            } catch (NumberFormatException e) {
                MagicLogger.log("Corrupted event id supplied");
                e.printStackTrace();
                response.status(500);
                return "failure";
            }
            DataManager rankingsDataManager = Main.getDataManager(EventRanking.class);
            List ranking = rankingsDataManager.find(EventRanking.COLUMN_EVENT_ID, eventId);
            response.status(200);
            MagicLogger.log("Successfully got %d rankings for event %d", ranking.size(), eventId);
            return Main.GSON.toJson(ranking);
        }
        response.status(500);
        return "failure";
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventRanking ranking = Main.GSON.fromJson(request.body(), EventRanking.class);
        Log.i( "RankingRoute", ranking.toString());

        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        if (rankingsDataManager.insertOrUpdate(ranking) == null) {
            response.status(500);
            Log.e("RankingRoute", "Failed to insert or update ranking for event " + ranking.getEventId() + ". Database error.");
            return "Failed to insert object";
        }

        response.status(200);
        return "success";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventRanking ranking = new EventRanking();
        ranking.setId(Long.parseLong(request.params(":id")));

        DataManager<EventRanking, String> rankingsDataManager = Main.getDataManager(EventRanking.class);
        if (!rankingsDataManager.delete(ranking)) {
            response.status(500);
            Log.e( "RankingRoute","Failed to delete ranking with id " + ranking.getId() + ". Database error.");
            return "Failed to delete object";
        }

        response.status(200);
        return "success";
    }
}
