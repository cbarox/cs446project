package ca.uwaterloo.mapapp.data.api.contracts.events;

import java.util.Date;

/**
 * Created by cjbarrac
 * 23/05/15
 * <p/>
 * This is just a super basic example of one of the GSON contracts we need.
 * This is a single instance of one of the holidays defined here:
 * https://github.com/uWaterloo/api-documentation/blob/master/v2/events/holidays.md
 * <p/>
 * For https://api.uwaterloo.ca/v2/events/holidays.json
 */
public class Holiday {
    public String name;
    public Date date;
}
