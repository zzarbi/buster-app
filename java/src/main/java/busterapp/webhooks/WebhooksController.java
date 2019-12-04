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

            transaction.ifPresent(trans -> {
                // only update status if the current one is one of those
                // and if the externalId matches
                if ( (trans.getStatus().equals("PENDING") || trans.getStatus().equals("CREATED"))
                    && trans.getExternal_id().equals(externalId)) {
                    DaoHelper.getTransactionDao().updateStatusByReferenceId(referenceId, status);
                }
            });
                
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