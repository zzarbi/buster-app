package busterapp;

import busterapp.index.IndexController;
import busterapp.transaction.*;
import busterapp.webhooks.*;
import busterapp.util.*;
import com.google.gson.JsonObject;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;
import static busterapp.util.Path.Web.*;

public class App {

    public static void main(String[] args) {
        if (EnvHelper.isDebug()) {
            System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
            System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
            System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");
        }

        // Configure Spark
        port(4567);
        staticFiles.location(PUBLIC);
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before(TRANSACTION, Filters.registerApplication);
        before((request, response) -> response.type("application/json")); // enforce every response to be json

        // Set up routes
        get(HEALTHCHECK, IndexController.healthcheck);
        post(TRANSACTION, TransactionController.create);
        get(TRANSACTION, TransactionController.fetchAll);
        post(WEBHOOKS, WebhooksController.handle);

        // handle other routes and error
        get("*", IndexController.notFound);
        internalServerError(IndexController.serverError);
        exception(Exception.class, (e, req, res) -> {
            JsonObject newResponse = new JsonObject();
            res.status(500);
            newResponse.addProperty("code", 500);
            newResponse.addProperty("error", e.getMessage());
            Logger.error("Fatal Error: " + e.getMessage());

            if (EnvHelper.isDebug()) {
                newResponse.addProperty("trace", e.getStackTrace().toString());
            }
            res.body(newResponse.toString());
        });

        // Initialize Ngrok
        Ngrok.getInstance();

        // Initialize Api to retrieve API key
        BusterApi.getInstance().register();
    }

}