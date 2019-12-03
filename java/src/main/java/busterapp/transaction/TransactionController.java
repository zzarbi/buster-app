package busterapp.transaction;

import com.google.gson.JsonObject;

import busterapp.util.BusterApi;
import spark.*;

public class TransactionController {

    public static Route fetchAll = (Request request, Response response) -> {
        JsonObject transaction =  BusterApi.getInstance().createTranscation();
        return transaction.toString();
    };

    public static Route create = (Request request, Response response) -> {
        return "post transacions";
    };
}