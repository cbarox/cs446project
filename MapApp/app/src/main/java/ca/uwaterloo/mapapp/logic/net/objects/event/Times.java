package ca.uwaterloo.mapapp.logic.net.objects.event;

import java.util.Date;

/**
 * Created by cjbarrac
 * 6/13/15
 */
public class Times {

    private Date start;
    private Date end;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Times{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
