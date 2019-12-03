package busterapp.webhooks;

import spark.*;

public class WebhooksController {

    public static Route handle = (Request request, Response response) -> {
        
        return "handled";
    };
}