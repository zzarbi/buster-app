package busterapp.index;

import com.google.gson.JsonObject;
import spark.*;

public class IndexController {

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
}