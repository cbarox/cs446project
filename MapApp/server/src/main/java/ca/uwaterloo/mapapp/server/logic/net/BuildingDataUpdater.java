package ca.uwaterloo.mapapp.server.logic.net;

import java.util.List;
import java.util.TimerTask;

import ca.uwaterloo.mapapp.server.Main;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.net.WaterlooApi;
import ca.uwaterloo.mapapp.shared.objects.building.Building;

/**
 * Created by cjbarrac
 * 7/1/15
 */
public class BuildingDataUpdater extends TimerTask {

    private static DataManager buildingDataManager = Main.getDataManager(Building.class);

    @Override
    public void run() {
        ICallback buildingsCallback = new ICallback() {
            @Override
            public void call(Object param) {
                List<Building> buildings = (List<Building>) param;
                buildingDataManager.insertOrUpdateAll(buildings);
            }
        };
        WaterlooApi.requestBuildings(buildingsCallback);
    }
}
