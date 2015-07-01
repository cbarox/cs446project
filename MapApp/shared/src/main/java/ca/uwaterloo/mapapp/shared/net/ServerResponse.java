package ca.uwaterloo.mapapp.shared.net;

import java.io.Serializable;

/**
 * Created by cjbarrac
 * 6/30/15
 */
public class ServerResponse implements Serializable {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILURE = "failure";

    private Long id;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
