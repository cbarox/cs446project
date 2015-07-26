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
    private static final DataManager buildingDataManager = Main.getDataManager(Building.class);
    private static final DataManager eventDataManager = Main.getDataManager(Event.class);

    @Override
    public void run() {
        final List<Building> buildingList = buildingDataManager.getAll();
        ICallback eventsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                List<Event> events = (List<Event>) param;
                eventDataManager.insertOrUpdateAll(events);
                for (Event event : events) {
                    addEventLocation(event, buildingList);
                }
            }
        };
        WaterlooApi.requestEvents(eventsCallback);
    }

    public void addEventLocation(final Event event, final List<Building> buildingList) {
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                String locationTextFromHtml = (String) param;
                String buildingCode = matchBuildingCode(buildingList, locationTextFromHtml);
                // Got a match on the location from event website
                if (buildingCode != null) {
                    event.setLocation(buildingCode);
                    eventDataManager.update(event);
                }
            }
        };

        // Try and get the event's location from the website
        String url = event.getLink();
        JSoupTask jSoupTask = new JSoupTask(url, MAGIC_CSS_SELECTOR, callback);
        Thread jSoupTaskThread = new Thread(jSoupTask);
        jSoupTaskThread.start();
    }

    public String matchBuildingCode(List<Building> buildings, String locationText) {
        for (Building building : buildings) {
            String buildingCode = building.getBuildingCode();
            if (buildingCode == null || locationText == null) {
                return null;
            }
            if (locationText.contains(buildingCode)) {
                return buildingCode;
            }
        }
        return null;
    }
}
