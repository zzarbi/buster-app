package busterapp.util;

import java.io.IOException;
import java.util.Date;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import spark.Request;

public class BusterApi {
    private static BusterApi instance = null;
    private String apiUrl = "";
    private String apiKey = "";

    /**
     * Private Constructor for singleton
     * 
     * @param String url
     * @param int version
     */
    private BusterApi(String url, int version) {
        this.apiUrl = url + "/v" + version;
    }

    /**
     * Send a request and add api header if needed
     * 
     * @param HttpUriRequest request
     * @param String apiKey
     * @return HttpResponse
     * @throws ClientProtocolException
     * @throws IOException
     */
    private JsonObject sendRequest(HttpUriRequest request, boolean needAuthentication) throws ClientProtocolException, IOException, Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        request.addHeader("content-type", "application/json");
        if (needAuthentication) {
            request.addHeader("X-API-KEY", apiKey);
        }

        // execute request
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception(responseBody.isEmpty() ? "Unkown error" : responseBody);
        }

        return JsonParser.parseString(responseBody).getAsJsonObject();
    }

    /**
     * Return an single and same instance of the API
     * @return BusterApi
     */
    public static BusterApi getInstance() {
        if (BusterApi.instance == null) {
            BusterApi.instance = new BusterApi(EnvHelper.getBusterAPIUrl(), Integer.parseInt(EnvHelper.getBusterAPIVersion()));
        }
        return BusterApi.instance;
    }

    /*
     * Register Application to retrieve an API key
     */
    public void register() {
        try {
            // create body json
            JsonObject data, body;
            body = new JsonObject();
            body.addProperty("webhookUrl", Ngrok.getInstance().getRemoteUrl() + Path.Web.WEBHOOKS);

            HttpPost request = new HttpPost(this.apiUrl + Path.Buster.API_KEY);
            request.setEntity(new StringEntity(body.toString()));

            // send non-authenticated request
            try {
                data = sendRequest(request, false);
            } catch(Exception e) {
                throw new Exception("Error, unable to retrieve API key");
            }

            if (!data.has("key") || data.get("key").getAsString().isEmpty()) {
                throw new Exception("Error: API Key is empty");
            }

            Logger.info("Got an API Key: " + data.get("key").toString());
            this.apiKey = data.get("key").getAsString();
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }

    /**
     * Private method that actually handle request to the transaction endpoint
     * - POST
     * - GET
     * 
     * @todo handleTransaction should be able to handle delete/put request
     * @param String referenceId
     * @param String type
     * @return JsonObject
     * @throws Exception
     */
    private JsonObject handleTransaction(String referenceId, String type) throws Exception {
        String url = this.apiUrl + Path.Buster.TRANSACTION;
        JsonObject data;

        try {
            if (type.equals("GET")) {
                HttpGet getRequest = new HttpGet(url + "?referenceId=" + referenceId);
                data = sendRequest(getRequest, true);
            } else {
                // Create json body request
                JsonObject body = new JsonObject();
                body.addProperty("referenceId", referenceId);

                HttpPost postRequest = new HttpPost(url);
                postRequest.setEntity(new StringEntity(body.toString()));

                data = sendRequest(postRequest, true);
            }
        } catch (Exception e) {
            // Should only happen when a transaction is created
            if (e.getMessage().equals("Reference Id is not unique")) {
                // attempt to retrieve it from buster-api
                return tryTransaction(referenceId, "GET", 150);
            }

            throw new Exception("Error on " + type + " " + Path.Buster.TRANSACTION + " with referenceId " + referenceId + " and API key: " + apiKey);
        }

        if (!data.has("id") || data.get("id").getAsString().isEmpty()) { // should never happen
            throw new Exception("Unknown error while retrieving transaction with API key: " + apiKey);
        }

        return data;
    }

    /**
     * Try a POST/GET request on the transaction endpoint and return the response
     * 
     * @todo tryTransaction should be able to handle delete/put request
     * @param String referenceId
     * @param String type
     * @param int maxTime, define how long it should try this transaction
     * @return JsonObject
     */
    public JsonObject tryTransaction(String referenceId, String type, int maxTime) {
        long start,now;
        start = now = (new Date()).getTime();
        JsonObject data = null;

        while(data == null && (now-start <= maxTime)) { // loop for some amount of milliseconds
            try {
                data = this.handleTransaction(referenceId, type);
            } catch (Exception ex) {
                now = (new Date()).getTime(); // update the time
                Logger.error(ex.getMessage());
            }
        }
        return data; // return data set
    }

    /**
     * Process data retrieved from a webhook
     * 
     * @param Request request
     * @return
     */
    public JsonObject handleWebhook(Request request) {
        String body = request.body();
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

    /**
     * Return true if the status is valid
     * 
     * @param String status
     * @return boolean
     */
    public boolean isStatusValid(String status) {
        switch(status) {
            case Path.Buster.STATUS_COMPLETED:
            case Path.Buster.STATUS_CANCELED:
            case Path.Buster.STATUS_PENDING:
            case Path.Buster.STATUS_CREATED:
                return true;
        }
        return false;
    }

    /**
     * APIKey getter
     * 
     * @return String
     */
    public String getAPIKey() {
        return this.apiKey;
    }
}
