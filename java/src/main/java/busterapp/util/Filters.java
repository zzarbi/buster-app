package busterapp.util;

import spark.*;

public class Filters {

    // If a request is made to transaction
    // we need to register the application with buster
    public static Filter registerApplication = (Request request, Response response) -> {
        BusterApi buster = BusterApi.getInstance();
        Logger.info("registerApplication");
        if (!buster.hasAPIKey()) {
            buster.register();
        }

        // verify API key
        if (!buster.hasAPIKey()) {
            throw new Exception("API Key missing, unrecoverable error");
        }
    };

}