package busterapp.index;

import java.util.Date;

import com.google.gson.JsonObject;

import busterapp.util.DateHelper;
import spark.*;

public class IndexController {

    /**
     * Action notFound
     */
    public static Route healthcheck = (Request request, Response response) -> {
        JsonObject newResponse = new JsonObject();
        response.status(200);
        newResponse.addProperty("code", 200);
        newResponse.addProperty("date", DateHelper.formatISO8601(new Date()));

        // enforce that the healthcheck is not cached
        response.header("Cache-Control", "no-cache");

        return newResponse.toString();
    };

    /**
     * Action notFound
     */
    public static Route notFound = (Request request, Response response) -> {
        JsonObject newResponse = new JsonObject();
        response.status(404);
        newResponse.addProperty("code", 404);
        newResponse.addProperty("error", "Route not found");
        
        return newResponse.toString();
    };

    /**
     * Action serverError
     */
    public static Route serverError = (Request request, Response response) -> {
        JsonObject newResponse = new JsonObject();
        response.status(500);
        newResponse.addProperty("code", 500);
        newResponse.addProperty("error", "Unknow Error");

        return newResponse.toString();
    };
}