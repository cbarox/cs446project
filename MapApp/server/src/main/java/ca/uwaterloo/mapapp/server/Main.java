package ca.uwaterloo.mapapp.server;

import ca.uwaterloo.mapapp.shared.MyClass;
import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {
        System.out.println(MyClass.HELLO);
        get("/hello", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return MyClass.HELLO;
            }
        });
    }
}
