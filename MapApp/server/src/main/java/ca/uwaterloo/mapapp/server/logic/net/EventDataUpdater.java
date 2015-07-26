package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;
import java.util.TimerTask;

import ca.uwaterloo.mapapp.server.MagicLogger;
import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.shared.objects.event.EventTimes;

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
                MagicLogger.log("Got %d events from the API", events.size());
                DataManager eventDataManager = Main.getDataManager(Event.class);
                eventDataManager.insertOrUpdateAll(events);
                for (Event event : events) {
                    addEventLocation(event, buildingList);
                    addEventTimes(event);
                }
            }
        };
        WaterlooApi.requestEvents(eventsCallback);
    }

    private void addEventTimes(Event event) {
        final Integer eventId = event.getId();
        DataManager eventTimesDataManager = Main.getDataManager(EventTimes.class);
        final List eventTimesList = eventTimesDataManager.find(EventTimes.COLUMN_EVENT_ID, eventId);

        // already have times in the db
        if (eventTimesList.size() > 0) {
            return;
        }
        MagicLogger.log("Adding %d event times to the database", eventTimesList.size());
        for (EventTimes eventTimes : event.getTimes()) {
            eventTimes.setEventId(eventId);
            eventTimesDataManager.insert(eventTimes);
        }

    }

    public void addEventLocation(final Event event, final List<Building> buildingList) {
        ICallback callback = new ICallback() {
            @Override
            public void call(Object param) {
                try {
                    MagicLogger.log("Processing %s", event.toString());
                    if (param == null) {
                        return;
                    }
                    String locationTextFromHtml = (String) param;
                    String buildingCode = matchBuildingCode(buildingList, locationTextFromHtml);
                    // Got a match on the location from event website
                    if (buildingCode != null) {
                        event.setLocation(buildingCode);
                        MagicLogger.log("Matched %s to %s", buildingCode, event.toString());
                        Main.getDataManager(Event.class).update(event);
                    }
                    MagicLogger.log("Done processing %s", event.toString());
                } catch (Exception e) {
                    MagicLogger.log("Error processing %s", event.toString());
                    e.printStackTrace();
                }
            }
        };

        // Try and get the event's location from the website
        String url = event.getLink();
        if (url != null && !url.isEmpty()) {
            JSoupTask jSoupTask = new JSoupTask(url, MAGIC_CSS_SELECTOR, callback);
            try {
                jSoupTask.run();
            } catch (Exception e) {
                MagicLogger.log("Error running jsoupTask from %s", url);
                e.printStackTrace();
            }
        }
        MagicLogger.log("Successfully ran jsoupTask from %s", url);
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
