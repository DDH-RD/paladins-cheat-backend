package dev.luzifer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.luzifer.paladins.PaladinsChampion;
import dev.luzifer.paladins.PaladinsChampionMapper;
import dev.luzifer.ui.AppStarter;
import dev.luzifer.web.WebClient;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private static final WebClient WEB_CLIENT = new WebClient();
    private static final PaladinsChampionMapper PALADINS_CHAMPION_MAPPER;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");

        Gson gson = new Gson();
        try {
            List<PaladinsChampion> champions = gson.fromJson(readChampsJson(), new TypeToken<List<PaladinsChampion>>(){}.getType());
            PALADINS_CHAMPION_MAPPER = new PaladinsChampionMapper(champions);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        Application.launch(AppStarter.class, args);
    }

    public static PaladinsChampionMapper getPaladinsChampionMapper() {
        return PALADINS_CHAMPION_MAPPER;
    }

    public static WebClient getWebClient() {
        return WEB_CLIENT;
    }

    private static String readChampsJson() throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("champs.json");

        if (inputStream == null) {
            throw new IOException("Could not load resource: champs.json");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        }
    }
}