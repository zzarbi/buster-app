package busterapp;

import static spark.Spark.get;

import busterapp.index.*;
import busterapp.transaction.*;
import busterapp.webhooks.*;
import busterapp.util.*;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class App {

    public static void main(String[] args) {
        System.setProperty("org.apache.commons.logging.Log","org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");

        // Configure Spark
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before(Path.Web.TRANSACTION, Filters.registerApplication);

        // Set up routes
        post(Path.Web.TRANSACTION, TransactionController.create);
        get(Path.Web.TRANSACTION, TransactionController.fetchAll);
        post(Path.Web.WEBHOOKS, WebhooksController.handle);
        get("*", IndexController.notFound);

        // Initialize Ngrok
        Ngrok.getInstance();

        // Initialize Api to retrieve API key
        BusterApi.getInstance().register();
    }

}