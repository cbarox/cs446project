package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;
import java.util.TimerTask;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventLocation;

/**
 * Created by cjbarrac
 * 7/1/15
 */
public class EventLocationUpdater extends TimerTask {

    private static DataManager eventLocationDataManager = Main.getDataManager(EventLocation.class);

    @Override
    public void run() {
        ICallback buildingsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                final List<Building> buildingList = (List<Building>) param;
                ICallback eventsCallback = new ICallback() {
                    @Override
                    public void call(Object param) {
                        List<Event> events = (List<Event>) param;
                        for (Event event : events) {
                            processEvent(event, buildingList);
                        }
                    }
                };
                WaterlooApi.requestEvents(eventsCallback);
            }
        };
        WaterlooApi.requestBuildings(buildingsCallback);
    }

    public void processEvent(final Event event, final List<Building> buildingList) {
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                String locationTextFromHtml = (String) param;
                String buildingCode = matchBuildingCode(buildingList, locationTextFromHtml);
                // Got a match on the location from event website
                if (buildingCode != null) {
                    EventLocation eventLocation = new EventLocation();
                    eventLocation.setEventId(event.getId());
                    eventLocation.setLocation(buildingCode);
                    eventLocationDataManager.insertOrUpdate(eventLocation);
                }
            }
        };

        // Try and get the event's location from the website
        String url = event.getLink();
        JSoupApi jSoupApi = new JSoupApi(url, "span.fn", callback);
        Thread jSoupApiThread = new Thread(jSoupApi);
        jSoupApiThread.start();
    }

    public String matchBuildingCode(List<Building> buildings, String locationText) {
        for (Building building : buildings) {
            String buildingCode = building.getBuildingCode();
            if (locationText.contains(buildingCode)) {
                return buildingCode;
            }
        }
        return null;
    }
}
