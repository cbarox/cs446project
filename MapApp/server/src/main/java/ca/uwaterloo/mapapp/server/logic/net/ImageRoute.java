package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;

import ca.uwaterloo.mapapp.server.MagicLogger;
import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class ImageRoute implements IGetSetDeleteRoute {

    @Override
    public Object get(Request request, Response response) throws Exception {
        final String eventIdString = request.params(":event");
        if (eventIdString == null) {
            MagicLogger.log("corrupted event id supplied");
            response.status(500);
            return "failure";
        }
        try {
            final int eventId = Integer.parseInt(eventIdString);
            DataManager imagesDataManager = Main.getDataManager(EventImage.class);
            List<EventImage> image = imagesDataManager.find(EventImage.COLUMN_EVENT_ID, eventId);
            response.status(200);
            MagicLogger.log("Successfully got images for event %d", eventId);
            return Main.GSON.toJson(image);

        } catch (NumberFormatException e) {
            MagicLogger.log("Failed to get images for event %d", eventIdString);
            e.printStackTrace();
        }
        response.status(500);
        return "failure";
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventImage image = Main.GSON.fromJson(request.body(), EventImage.class);
        MagicLogger.log(image.toString());

        DataManager imagesDataManager = Main.getDataManager(EventImage.class);
        if (imagesDataManager.insertOrUpdate(image) == null) {
            response.status(500);
            MagicLogger.log("Failed to update %s", image.toString());
            return "failure";
        }
        MagicLogger.log("Successfully updated %s", image.toString());
        response.status(200);
        return "success";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventImage image = new EventImage();
        image.setId(Long.parseLong(request.params(":id")));

        DataManager imagesDataManager = Main.getDataManager(EventImage.class);
        if (!imagesDataManager.delete(image)) {
            response.status(500);
            MagicLogger.log("Failed to delete %s", image.toString());
            return "failure";
        }
        MagicLogger.log("Successfully deleted %s", image.toString());
        response.status(200);
        return "success";
    }
}
