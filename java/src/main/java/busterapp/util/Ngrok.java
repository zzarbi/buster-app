package busterapp.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Ngrok {
    private static Ngrok instance = null;
    private String remoteUrl = "";
    private String apiScheme = "http";

    private Ngrok () {
        init();
    }

    private String getFullUrl() {
        return apiScheme + "://" + EnvHelper.getNgrokHost() + ":" + EnvHelper.getNgrokPort();
    }

    /*
     * Return an instance of the API
     */
    public static Ngrok getInstance() {
        if (Ngrok.instance == null) {
            Ngrok.instance = new Ngrok();
        }
        return Ngrok.instance;
    }

    /*
     * Register Application to retrieve an API key
     */
    public void init() {
        Logger.info("Initializing Ngrok");
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpGet request = new HttpGet(getFullUrl() + "/api/tunnels");

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new Exception("Ngrok service is unavailable");
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            String allMatches[] = new String[5];
            Matcher m = Pattern.compile("http://([^\\./]+)\\.ngrok\\.io")
                .matcher(responseBody);
    
            int i = 0;
            while (m.find()) {
                allMatches[i] = m.group();
                i++;
            }
    
            remoteUrl = allMatches[0];

            Logger.info("Ngrok Remote URL: " + remoteUrl);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }

    /**
     * Get the remote URL that ngrok uses
     * 
     * @return String
     */
    public String getRemoteUrl() {
        return remoteUrl;
    }
}
