package busterapp.transaction;

import java.util.List;
import java.util.Optional;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import busterapp.transaction.model.Transaction;
import busterapp.util.BusterApi;
import busterapp.util.DaoHelper;
import spark.*;

public class TransactionController {

    /**
     * Action fetchAll for transaction
     */
    public static Route fetchAll = (Request request, Response response) -> {
        // get last 10 transactions
        List<Transaction> transRawList = DaoHelper.getTransactionDao().getAllReversed(10, 0);

        // Convert it to a JsonElements [{Transaction},{{Transaction}}]
        JsonArray transactions = new JsonArray();
        transRawList.forEach((trans) -> {
            transactions.add(trans.toJson());
		});

        JsonObject newResponse = new JsonObject();
        newResponse.addProperty("code", 200);
        newResponse.getAsJsonObject().add("transactions", transactions);

        return newResponse.toString();
    };

    /**
     * Action create for transaction
     */
    public static Route create = (Request request, Response response) -> {
        // todo need to handle errors
        JsonObject resultFromApi = BusterApi.getInstance().createTranscation();
        JsonObject newResponse = new JsonObject();
        
        if (resultFromApi.has("id")) {
            String referenceId = resultFromApi.get("referenceId").getAsString();
            String externalId = resultFromApi.get("id").getAsString();
            String status = resultFromApi.get("status").getAsString();

            // save records to DB
            DaoHelper.getTransactionDao().create(referenceId, externalId, status);

            newResponse.addProperty("code", 200);
            Optional<Transaction> transactionModel = DaoHelper.getTransactionDao().getByReferenceId(referenceId);
            transactionModel.ifPresent(trans -> {
                newResponse.getAsJsonObject().add("transaction", trans.toJson());
            });
        } else {
            newResponse.addProperty("code", 400);
            newResponse.addProperty("error", "Unable to create transaction");
        }
        
        return newResponse.toString(); 
    };
}