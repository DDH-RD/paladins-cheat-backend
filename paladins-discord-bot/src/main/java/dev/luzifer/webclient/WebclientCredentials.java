package dev.luzifer.webclient;

public class WebclientCredentials {

    private final String apiKey;
    private final String ipAdress;

    public WebclientCredentials(String apiKey, String ipAdress) {
        this.apiKey = apiKey;
        this.ipAdress = ipAdress;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getIpAdress() {
        return ipAdress;
    }
}
