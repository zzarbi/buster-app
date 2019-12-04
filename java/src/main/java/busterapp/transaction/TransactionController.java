package busterapp.transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import busterapp.transaction.model.Transaction;
import busterapp.util.BusterApi;
import busterapp.util.DaoHelper;
import busterapp.util.Path;
import spark.*;

public class TransactionController {
    /**
     * Read integer from the query params
     * return default if not found
     * 
     * @param Request request
     * @param String name
     * @param int defaultValue
     * @return
     */
    private static int getIntQueryParam(Request request, String name, int defaultValue) {
        try {
            int value = Integer.parseInt(request.queryParams(name));
            if (value <= 0) {
                value = 0;
            }
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Action fetchAll for transaction
     */
    public static Route fetchAll = (Request request, Response response) -> {
        int limit = TransactionController.getIntQueryParam(request, "limit", 100);
        int offset = TransactionController.getIntQueryParam(request, "offset", 0);

        // get last 10 transactions
        List<Transaction> transRawList = DaoHelper.getTransactionDao().getAllReversed(limit, offset);

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
        // try to create a transaction for at least 250ms
        String referenceId = UUID.randomUUID().toString().replace("-", ""); // create a uniqueId
        JsonObject resultFromApi = BusterApi.getInstance().tryTransaction(referenceId, "POST", 250);
        JsonObject newResponse = new JsonObject();
        
        if (resultFromApi != null && resultFromApi.has("id")) {
            referenceId = resultFromApi.get("referenceId").getAsString();
            String externalId = resultFromApi.get("id").getAsString();
            String status = resultFromApi.get("status").getAsString();

            if (!BusterApi.getInstance().isStatusValid(status)) {
                status = Path.Buster.STATUS_CREATED; // If status is wrong, ensure it's set to created
            }

            // save records to DB
            DaoHelper.getTransactionDao().create(referenceId, externalId, status);

            newResponse.addProperty("code", 201);
            response.status(201);
            Optional<Transaction> transactionModel = DaoHelper.getTransactionDao().getByReferenceId(referenceId);
            transactionModel.ifPresent(trans -> {
                newResponse.getAsJsonObject().add("transaction", trans.toJson());
            });
        } else {
            response.status(503);
            response.header("Retry-After", "1"); // set header to retry after 1second
            newResponse.addProperty("code", 503);
            newResponse.addProperty("error", "Unable to create transaction at the moment");
        }
        
        return newResponse.toString(); 
    };
}