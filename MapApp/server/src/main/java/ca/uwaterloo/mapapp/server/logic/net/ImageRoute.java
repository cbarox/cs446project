package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class ImageRoute implements IGetSetDeleteRoute {
    private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @Override
    public Object get(Request request, Response response) throws Exception {
        DataManager<EventImage, String> imagesDataManager = Main.getDataManager(EventImage.class);
        EventImage image = imagesDataManager.findFirst(EventImage.COLUMN_EVENT_ID, Integer.parseInt(request.params(":event")));
        response.status(200);
        return gson.toJson(image);
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        EventImage image = gson.fromJson(request.body(), EventImage.class);
        System.out.println(image.toString());

        DataManager<EventImage, String> imagesDataManager = Main.getDataManager(EventImage.class);
        if( imagesDataManager.insertOrUpdate(image) == null ) {
            response.status(500);
            System.err.println("Failed to insert or update image for event " + image.getEventId() + ". Database error.");
            return "Failed to insert object";
        }
        System.out.println("Successfully inserted or updated image for event with id " + image.getEventId());
        response.status(200);
        return "";
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        EventImage image = new EventImage();
        image.setId(Long.parseLong(request.params(":id")));

        DataManager<EventImage, String> imagesDataManager = Main.getDataManager(EventImage.class);
        if( !imagesDataManager.delete(image) ) {
            response.status(500);
            System.err.println("Failed to delete image with id " + image.getId() + ". Database error.");
            return "Failed to delete object";
        }
        System.out.println("Successfully deleted image with id " + image.getId() );
        response.status(200);
        return "";
    }
}
