package ca.uwaterloo.mapapp.logic.net.services;
import java.util.List;

import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.shared.objects.event.EventLocation;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import ca.uwaterloo.mapapp.shared.objects.event.EventTimes;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by cjbarrac
 * 24/05/15
 */
public interface IServerRestService {

    String API_ENDPOINT = "http://104.236.77.229:4567"; /* our server */

    @GET("/events")
    List<Event> getEvents();

    @GET("/event_images")
    List<EventImage> getEventImages();
   
    @GET("/event_locations")
    List<EventLocation> getEventLocations();
   
    @GET("/event_times")
    List<EventTimes> getEventTimes();
   
    @GET("/note/get/{id}")
    List<EventNote> getEventNotes(@Path("id") Long eventId);

    @POST("/note/set")
    Response setEventNote(@Body EventNote note);

    @POST("/note/delete/{id}")
    Response deleteEventNote(@Path("id") Long eventNoteId);

    @GET("/ranking/get/{id}")
    EventRanking getEventRanking(@Path("id") Long eventId);

    @POST("/ranking/set")
    Response setEventRanking(@Body EventRanking ranking);

    @POST("/ranking/delete/{id}")
    Response deleteEventRanking(@Path("id") Long rankingId);
}
