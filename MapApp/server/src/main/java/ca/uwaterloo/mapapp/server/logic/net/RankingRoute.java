package ca.uwaterloo.mapapp.server.logic.net;

import com.google.gson.Gson;

import ca.uwaterloo.mapapp.shared.net.ServerResponse;
import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public class RankingRoute implements IGetSetDeleteRoute {
    private static Gson gson = new Gson();

    @Override
    public Object get(Request request, Response response) throws Exception {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus("success");
        serverResponse.setId(0L);
        return gson.toJson(serverResponse);
    }

    @Override
    public Object set(Request request, Response response) throws Exception {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus("success");
        serverResponse.setId(0L);
        return gson.toJson(serverResponse);
    }

    @Override
    public Object delete(Request request, Response response) throws Exception {
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setStatus("success");
        serverResponse.setId(0L);
        return gson.toJson(serverResponse);
    }
}
