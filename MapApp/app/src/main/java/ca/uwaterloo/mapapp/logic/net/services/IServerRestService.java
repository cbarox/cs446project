package ca.uwaterloo.mapapp.logic.net.services;
import java.util.List;

import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.shared.objects.event.EventLocation;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventTimes;

import retrofit.http.GET;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public interface IServerRestService {

    String API_ENDPOINT = "104.236.77.229:4567 "; /* our server */

    @GET("/events")
    List<Event> getEvents();

    @GET("/event_images")
    List<EventImage> getEventImages();
   
    @GET("/event_locations")
    List<EventLocation> getEventLocations();
   
    @GET("/event_times")
    List<EventTimes> getEventTimes();
   
    @GET("/events_notes")
    List<EventNote> getEventNotes();
}
