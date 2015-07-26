package ca.uwaterloo.mapapp.logic.net.services;

import java.util.List;

import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventImage;
import ca.uwaterloo.mapapp.shared.objects.event.EventNote;
import ca.uwaterloo.mapapp.shared.objects.event.EventRanking;
import ca.uwaterloo.mapapp.shared.objects.event.EventTimes;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
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

    @GET("/image")
    List<EventImage> getEventImages();

    @POST("/image")
    Response setEventImage(@Body EventImage image);

    @DELETE("/image")
    Response deleteEventImage(@Path("id") Long rankingId);

    @GET("/event_times")
    List<EventTimes> getEventTimes();

    @GET("/note/{id}")
    List<EventNote> getEventNotes(@Path("id") Integer eventId);

    @POST("/note")
    Response setEventNote(@Body EventNote note);

    @DELETE("/note/{id}")
    Response deleteEventNote(@Path("id") Long eventNoteId);

    @GET("/ranking/{id}")
    EventRanking getEventRanking(@Path("id") Integer eventId);

    @POST("/ranking")
    Response setEventRanking(@Body EventRanking ranking);

    @DELETE("/ranking/{id}")
    Response deleteEventRanking(@Path("id") Long rankingId);
}
