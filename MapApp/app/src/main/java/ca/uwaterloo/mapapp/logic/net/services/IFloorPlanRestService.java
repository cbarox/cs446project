package ca.uwaterloo.mapapp.logic.net.services;


import java.util.List;

import ca.uwaterloo.mapapp.objects.floorplan.FloorPlanDatabase;
import ca.uwaterloo.mapapp.objects.floorplan.Room;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by brwarner
 * 24/05/15
 */
public interface IFloorPlanRestService {
    String API_ENDPOINT = "http://104.236.77.229:8000";

    @GET("/database.json")
    List<FloorPlanDatabase> getFloorPlanDatabases();

    @GET("/png/{floorplan}.json")
    List<Room> getRooms(@Path("floorplan") String floorplan);
}
