package busterapp.transaction.model;

import lombok.Data;
import java.util.Date;
import com.google.gson.JsonObject;

import busterapp.util.DateHelper;

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
        JsonObject obj = new JsonObject();
        obj.addProperty("id", id);
        obj.addProperty("reference_id", reference_id);
        obj.addProperty("external_id", external_id);
        obj.addProperty("created", DateHelper.formatISO8601(created));
        obj.addProperty("status", status);

        return obj;
    }
}