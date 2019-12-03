package busterapp.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String TRANSACTION = "/transaction";
        @Getter public static final String WEBHOOKS = "/webhooks";
    }

}