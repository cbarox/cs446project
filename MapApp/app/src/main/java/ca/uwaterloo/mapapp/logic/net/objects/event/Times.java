package ca.uwaterloo.mapapp.logic.net.objects.event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uwaterloo.mapapp.logic.Logger;

/**
 * Created by cjbarrac
 * 6/13/15
 */
public class Times {

    private String start;
    private String end;

    public Date getStart() {
        return getDate(start);
    }

    private Date getDate(String date) {
        String[] split = date.split("\\+");
        String timezone = split[1].replace(":", "");
        String dateString = split[0] + "+" + timezone;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Logger.error("Failed to parse date");
        }
        return null;
    }

    public Date getEnd() {
        return getDate(end);
    }

}
