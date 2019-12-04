package busterapp.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // endpoints for this web app
    public static class Web {
        @Getter public static final String PUBLIC = "/public";
        @Getter public static final String HEALTHCHECK = "/healthcheck";
        @Getter public static final String TRANSACTION = "/transaction";
        @Getter public static final String WEBHOOKS = "/webhooks";
    }

    // The @Getter methods are needed in order to access
    // endpoints for ngrok
    public static class Ngrok {
        @Getter public static final String API_TUNNELS = "/api/tunnels";
    }

    // The @Getter methods are needed in order to access
    // endpoints for Buster
    public static class Buster {
        // endpoints
        @Getter public static final String API_KEY = "/api_key";
        @Getter public static final String TRANSACTION = "/transaction";

        // statuses
        @Getter public static final String STATUS_CREATED = "CREATED";
        @Getter public static final String STATUS_PENDING = "PENDING";
        @Getter public static final String STATUS_CANCELED = "CANCELED";
        @Getter public static final String STATUS_COMPLETED = "COMPLETED";
    }
}