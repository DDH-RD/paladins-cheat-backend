package dev.luzifer.webclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webclient {

    private static final String COUNT_URL = "/game/get/count";
    private static final String COUNT_CHAMPS_URL = COUNT_URL + "/champs";
    private static final int PORT = 8080;

    private final WebclientCredentials credentials;
    private final String url;

    public Webclient(WebclientCredentials credentials) {
        this.credentials = credentials;
        this.url = "http://" + credentials.getIpAdress() + ":" + PORT + "/" +  credentials.getApiKey();
    }

    public int makeCountRequest() {
        String countUrl = url + COUNT_URL;
        return Integer.parseInt(makeRequest(countUrl));
    }

    public int makeCountChampsRequest() {
        String countChampsUrl = url + COUNT_CHAMPS_URL;
        return Integer.parseInt(makeRequest(countChampsUrl));
    }

    private String makeRequest(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if(responseCode != 200) {
                System.err.println("Could not make request to " + url + " (Response code: " + responseCode + ")");
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (IOException e) {
            System.err.println("Could not make request to " + url);
            e.printStackTrace();
            return null;
        }
    }
}
