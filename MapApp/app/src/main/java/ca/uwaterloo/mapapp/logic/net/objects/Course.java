package ca.uwaterloo.mapapp.logic.net.objects;

/**
 * Created by Hyunwook on 2015-06-13.
 */
import java.util.List;

/**
 * Created by Hyunwook on 2015-06-13.
 */

class Location {
    private String building;
    private String room;
}

class Dates {
    private String startTime;
    private String endTime;
    private String weekdays;
}

class Class {
    public Dates dates;
    public Location location;
}

public class Course {
    private String subject;
    private String title;
    private double classNumber;
    private List<Class> classes;
}
