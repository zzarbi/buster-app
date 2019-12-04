package busterapp.util;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import spark.Request;

public class BusterApi {
    private static BusterApi instance = null;
    private String apiUrl = "";
    private String apiKey = "";

    private BusterApi (String url, int version) {
        this.apiUrl = url + "/v" + version + "/";
    }

    /*
     * Return an instance of the API
     */
    public static BusterApi getInstance() {
        if (BusterApi.instance == null) {
            BusterApi.instance = new BusterApi("http://34.102.239.194", 1);
        }
        return BusterApi.instance;
    }

    /*
     * Register Application to retrieve an API key
     */
    public void register() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        JsonObject body = new JsonObject();
        String webhookUrl = Ngrok.getInstance().getRemoteUrl() + "/webhooks";
        body.addProperty("webhookUrl", webhookUrl);

        try {
            HttpPost request = new HttpPost(this.apiUrl + "api_key");
            Logger.info("API: " + this.apiUrl + "api_key");
            
            StringEntity params = new StringEntity(body.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new Exception("Unable to get register this application");
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject data = JsonParser.parseString(responseBody).getAsJsonObject();

            if (!data.has("key") || data.get("key").getAsString().isEmpty()) {
                throw new Exception("API Key is empty");
            }
            
            Logger.info("Got an API Key: " + data.get("key").toString());
            this.apiKey = data.get("key").getAsString();
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }

    /**
     * Create a transaction and return the response
     * 
     * @return JsonObject
     */
    public JsonObject createTranscation() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String apiKey = BusterApi.getInstance().getAPIKey();
        JsonObject body = new JsonObject();
        body.addProperty("referenceId", UUID.randomUUID().toString().replace("-", ""));

        try {
            HttpPost request = new HttpPost(this.apiUrl + "transaction");
            StringEntity params = new StringEntity(body.toString());
            request.addHeader("content-type", "application/json");
            request.addHeader("X-API-KEY", apiKey);
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new Exception("HTTP Error: " + statusCode + ", while creating transaction " + body.get("referenceId").toString() +" with API key: " + apiKey);
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject data = JsonParser.parseString(responseBody).getAsJsonObject();
            Logger.info("Response: " + responseBody);

            if (!data.has("id") || data.get("id").getAsString().isEmpty()) {
                throw new Exception("Unknown error while retrieving transaction with API key: " + apiKey);
            }
            
            return data; // return data set
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return new JsonObject();
        }
    }

    public JsonObject handleWebhook(Request response) {
        String body = response.body();
        Logger.info("Webhook Received: " + body);
        JsonObject data = JsonParser.parseString(body).getAsJsonObject();

        if (data.has("type")) {
            String transactionType = data.get("type").getAsString();

            if (transactionType.equals("TRANSACTION_UPDATE")) {
                return data.getAsJsonObject("data");
            }
        }
        return null;
    }

    public String getAPIKey() {
        return this.apiKey;
    }

    public boolean hasAPIKey() {
        return (this.apiKey != "");
    }
}
