package ca.uwaterloo.mapapp.server.logic.net;

import spark.Request;
import spark.Response;

/**
 * Created by brwarner2 on 20/07/2015.
 */
public interface IGetSetDeleteRoute {
    Object get(Request request, Response response) throws Exception;
    Object set(Request request, Response response) throws Exception;
    Object delete(Request request, Response response) throws Exception;
}
