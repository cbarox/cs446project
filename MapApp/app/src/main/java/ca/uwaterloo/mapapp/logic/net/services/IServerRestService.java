package ca.uwaterloo.mapapp.logic.net.services;


import java.util.List;

import ca.uwaterloo.mapapp.shared.objects.event.Event;
import retrofit.http.GET;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public interface IServerRestService {

    String API_ENDPOINT = "104.236.77.229:4567 "; /* our server */

    @GET("/events")
    List<Event> getEvents();
}
