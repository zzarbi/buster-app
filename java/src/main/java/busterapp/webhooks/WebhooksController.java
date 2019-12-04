package busterapp.webhooks;

import java.util.Optional;
import com.google.gson.JsonObject;
import busterapp.transaction.model.Transaction;
import busterapp.util.BusterApi;
import busterapp.util.DaoHelper;
import busterapp.util.Path;
import spark.*;
import org.sql2o.*;

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
                // only update status if:
                // - the status is valid
                // - the current status is not canceled/completed
                // - the externalId matches
                if ( BusterApi.getInstance().isStatusValid(status)
                    && !transaction.get().getStatus().equals(Path.Buster.STATUS_CANCELED)
                    && !transaction.get().getStatus().equals(Path.Buster.STATUS_COMPLETED)
                    && transaction.get().getExternal_id().equals(externalId)) {
                    DaoHelper.getTransactionDao().updateStatusByReferenceId(referenceId, status);
                }
            } else { // in the case the object does not exist in the DB, create it
                if (!BusterApi.getInstance().isStatusValid(status)) { // if status is invalid for somereason
                    status = Path.Buster.STATUS_CREATED;
                }

                try {
                    DaoHelper.getTransactionDao().create(referenceId, externalId, status);
                } catch(Sql2oException e) {
                    // if there is an error and it's not due to duplication
                    if (!e.getMessage().contains("Duplicate entry")) {
                        throw e;
                    }
                }
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