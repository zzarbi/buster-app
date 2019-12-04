package busterapp.util;

import spark.*;

public class Filters {
    
    /**
     * If a request is made to transaction
     * we need to register the application with buster
     */
    public static Filter registerApplication = (Request request, Response response) -> {
        BusterApi buster = BusterApi.getInstance();
        Logger.info("registerApplication");
        if (buster.getAPIKey().isEmpty()) {
            buster.register();
        }

        // verify API key
        if (buster.getAPIKey().isEmpty()) {
            throw new Exception("API Key missing, unrecoverable error");
        }
    };

}