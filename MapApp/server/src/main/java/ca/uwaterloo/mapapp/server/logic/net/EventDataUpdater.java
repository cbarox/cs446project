package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;
import java.util.TimerTask;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;

/**
 * Created by cjbarrac
 * 7/1/15
 */
public class EventDataUpdater extends TimerTask {

    private static final String MAGIC_CSS_SELECTOR = "span.fn";

    @Override
    public void run() {
        DataManager buildingDataManager = Main.getDataManager(Building.class);
        final List<Building> buildingList = buildingDataManager.getAll();
        ICallback eventsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                List<Event> events = (List<Event>) param;
                System.out.printf("Got %d events from the API%n", events.size());
                DataManager eventDataManager = Main.getDataManager(Event.class);
                eventDataManager.insertOrUpdateAll(events);
                for (Event event : events) {
                    addEventLocation(event, buildingList);
                }
                System.out.println("Done processing events");
            }
        };
        WaterlooApi.requestEvents(eventsCallback);
    }

    public void addEventLocation(final Event event, final List<Building> buildingList) {
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                try {
                    System.out.printf("Processing %s%n", event.toString());
                    if (param == null) {
                        return;
                    }
                    String locationTextFromHtml = (String) param;
                    String buildingCode = matchBuildingCode(buildingList, locationTextFromHtml);
                    // Got a match on the location from event website
                    if (buildingCode != null) {
                        event.setLocation(buildingCode);
                        System.out.printf("Matched %s to %s%n", buildingCode, event.toString());
                        Main.getDataManager(Event.class).update(event);
                    }
                    System.out.printf("Done processing %s%n", event.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Try and get the event's location from the website
        String url = event.getLink();
        if (url != null && !url.isEmpty()) {
            JSoupTask jSoupTask = new JSoupTask(url, MAGIC_CSS_SELECTOR, callback);
            try {
                Thread jSoupTaskThread = new Thread(jSoupTask);
                jSoupTaskThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String matchBuildingCode(List<Building> buildings, String locationText) {
        for (Building building : buildings) {
            if (building == null) {
                return null;
            }
            String buildingCode = building.getBuildingCode();
            if (buildingCode == null) {
                return null;
            }
            if (locationText.contains(buildingCode)) {
                return buildingCode;
            }
        }
        return null;
    }
}
