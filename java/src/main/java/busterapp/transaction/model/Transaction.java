package busterapp.transaction.model;

import lombok.Data;

import java.util.Date;

import com.google.gson.JsonObject;

@Data
public class Transaction {
    private int id;
    private String reference_id;
    private String external_id;
    private Date created;
    private String status;

    /**
     * Convert to a JsonObject
     * 
     * @return JsonObject
     */
    public JsonObject toJson() {
        JsonObject transObj = new JsonObject();
        transObj.addProperty("id", id);
        transObj.addProperty("reference_id", reference_id);
        transObj.addProperty("external_id", external_id);
        transObj.addProperty("created", created.toString());
        transObj.addProperty("status", status);

        return transObj;
    }
}