package busterapp.webhooks;

import java.util.Optional;
import com.google.gson.JsonObject;
import busterapp.transaction.model.Transaction;
import busterapp.util.BusterApi;
import busterapp.util.DaoHelper;
import spark.*;

public class WebhooksController {

    /**
     * Action handle for Webhooks
     */
    public static Route handle = (Request request, Response response) -> {
        JsonObject data = BusterApi.getInstance().handleWebhook(request);
        JsonObject newResponse = new JsonObject();

        if (data != null) {
            String status = data.get("status").getAsString();
            String referenceId = data.get("referenceId").getAsString();
            String externalId = data.get("id").getAsString();
            
            Optional<Transaction> transaction = DaoHelper.getTransactionDao().getByReferenceId(referenceId);

            if (transaction.isPresent()) {
                // only update status if the current one is one of those
                // and if the externalId matches
                if ( (transaction.get().getStatus().equals("PENDING") || transaction.get().getStatus().equals("CREATED"))
                    && transaction.get().getExternal_id().equals(externalId)) {
                    DaoHelper.getTransactionDao().updateStatusByReferenceId(referenceId, status);
                }
            } else { // in the case the object does not exist in the DB, create it
                DaoHelper.getTransactionDao().create(referenceId, externalId, status);
                // Potentially coulde return 201 code, since we created it
            }
                
            // unless the input data is misformated or incorrect.
            // always send 200 
            newResponse.addProperty("code", 200);
        } else {
            response.status(400);
            newResponse.addProperty("code", 400);
            newResponse.addProperty("error", "Data is incorrect OR transaction does not exist");
        }

        return newResponse.toString();
    };
}