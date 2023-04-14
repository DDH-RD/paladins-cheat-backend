package dev.luzifer.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class WebClient {

    private static final Logger WEB_CLIENT_LOGGER = Logger.getLogger(WebClient.class.getName());

    private static final String BASE_URL = "http://202.61.202.50:8080/THV6aSBpc3QgZWluIFPDvMOfaQ==";
    private static final String COUNT_PATH = "/game/get/count";
    private static final String DECK_PATH = "/game/get/bestDeckForChamp/";
    private static final String TALENT_PATH = "/game/get/bestTalentForChamp/";

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<Integer, Integer>>(){}.getType();

    private final OkHttpClient client = new OkHttpClient();

    public Integer count() {
        String url = buildUrl(COUNT_PATH);

        try (Response response = makeRequest(url)) {
            if(response.code() != 302) {
                WEB_CLIENT_LOGGER.warning("Could not get count from server [status code: " + response.code() + "]");
                return -1;
            }
            String json = response.body().string();
            WEB_CLIENT_LOGGER.info("Got count from server: " + json);
            return GSON.fromJson(json, Integer.class) / 10;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> requestDeck(int championId) {
        String path = DECK_PATH + championId;
        String url = buildUrl(path);

        try (Response response = makeRequest(url)) {
            if(response.code() != 302) {
                WEB_CLIENT_LOGGER.warning("Could not get deck from server [status code: " + response.code() + "]");
                return Collections.EMPTY_MAP;
            }
            String json = response.body().string();
            WEB_CLIENT_LOGGER.info("Got deck from server: " + json);
            return GSON.fromJson(json, MAP_TYPE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> requestTalent(int championId) {
        String path = TALENT_PATH + championId;
        String url = buildUrl(path);

        try (Response response = makeRequest(url)) {
            if(response.code() != 302) {
                WEB_CLIENT_LOGGER.warning("Could not get talent from server [status code: " + response.code() + "]");
                return Collections.EMPTY_MAP;
            }
            String json = response.body().string();
            WEB_CLIENT_LOGGER.info("Got talent from server: " + json);
            return GSON.fromJson(json, MAP_TYPE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildUrl(String path) {
        return BASE_URL + path;
    }

    private Response makeRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }
}
