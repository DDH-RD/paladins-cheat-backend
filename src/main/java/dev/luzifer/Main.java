package dev.luzifer;

import dev.luzifer.data.match.info.CardInfo;
import dev.luzifer.data.match.info.ChampInfo;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.ItemInfo;
import dev.luzifer.data.match.info.TeamInfo;
import dev.luzifer.json.JsonUtil;
import dev.luzifer.spring.Application;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

public class Main {

    public static final Logger LOGGER = Logger.getLogger("Paladins");

    public static void main(String[] args) {
        // createSampleJsonFile();
        SpringApplication.run(Application.class);
    }

    private static void createSampleJsonFile() throws IOException {

        GameInfo gameInfo = new GameInfo(
                "map_name",
                5, new long[] {1,5,2,1},
                new TeamInfo(4,
                        new ChampInfo[] {
                                new ChampInfo(
                                        1,
                                        1,
                                        1,
                                        new CardInfo[] {
                                                new CardInfo(1, 2)
                                        },
                                        new ItemInfo[] {
                                                new ItemInfo(1,1)
                                        }
                                        , 1
                                        , 2
                                        , 3
                                        , 4
                                        , 5
                                        , 6
                                        , 7
                                        , 8)}),
                new TeamInfo(2,
                        new ChampInfo[] {
                                new ChampInfo(
                                        4,
                                        3,
                                        2,
                                        new CardInfo[] {
                                                new CardInfo(1, 2)
                                        },
                                        new ItemInfo[] {
                                                new ItemInfo(1,1)
                                        }
                                        , 1
                                        , 2
                                        , 3
                                        , 4
                                        , 5
                                        , 6
                                        , 7
                                        , 8)}),
                900000,
                Instant.now().toEpochMilli());

        File file = new File("gameinfo.json");
        if(!file.exists())
            file.createNewFile();

        try(FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(JsonUtil.toJson(gameInfo));
            fileWriter.flush();
        }
    }

}
