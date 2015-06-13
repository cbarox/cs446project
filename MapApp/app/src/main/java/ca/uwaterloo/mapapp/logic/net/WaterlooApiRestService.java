package ca.uwaterloo.mapapp.logic.net;


import java.util.List;

import ca.uwaterloo.mapapp.logic.net.objects.Building;
import ca.uwaterloo.mapapp.logic.net.objects.event.Event;
import retrofit.http.GET;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public interface WaterlooApiRestService {

    String API_KEY = "22e67ab71cbd805f4f2dbe9d89fd7286";
    String API_ENDPOINT = "https://api.uwaterloo.ca/v2";

    @GET("/buildings/list.json?key=" + API_KEY)
    List<Building> getBuildings();

    @GET("/events.json?key=" + API_KEY)
    List<Event> getEvents();
}
