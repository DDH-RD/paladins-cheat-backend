package dev.luzifer.ui.view.viewmodels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.luzifer.ui.view.ViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class PaladinsClientViewModel implements ViewModel {

    private static final String WEB_PATH_COUNT = "http://202.61.202.50:8080/luziiststockschwul/game/get/count";
    private static final String WEB_PATH_DECK = "http://202.61.202.50:8080/luziiststockschwul/game/get/bestDeckForChamp/";
    private static final String WEB_PATH_TALENT = "http://202.61.202.50:8080/luziiststockschwul/game/get/bestTalentForChamp/";
    private static final Gson gson = new Gson();

    public int count() {

        String webPath = WEB_PATH_COUNT + "?matchType=RANKED";
        Request request = new Request.Builder()
                .url(webPath)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            return gson.fromJson(json, Integer.class) / 10;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Map<Integer, Integer> requestDeck(int championId) {

        String webPath = WEB_PATH_DECK + championId + "?matchType=RANKED";
        Request request = new Request.Builder()
                .url(webPath)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            Type type = new TypeToken<Map<Integer, Integer>>(){}.getType();
            return gson.fromJson(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> requestTalent(int championId) {
        String webPath = WEB_PATH_TALENT + championId + "?matchType=RANKED";
        Request request = new Request.Builder()
                .url(webPath)
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            Type type = new TypeToken<Map<Integer, Integer>>(){}.getType();
            return gson.fromJson(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
