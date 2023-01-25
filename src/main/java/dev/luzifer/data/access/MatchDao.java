package dev.luzifer.data.access;

import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.json.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component("matchDao")
public class MatchDao {

    private final Database database = new Database();

    public void save(MatchId matchId, GameInfo gameInfo) {
        CompletableFuture.runAsync(() -> database.insert(matchId.getId(), JsonUtil.toJson(gameInfo)));
    }

    public void saveMultiple(Map<MatchId, GameInfo> map) {
        CompletableFuture.runAsync(() -> database.insertMultiple(map));
    }

    public CompletionStage<Map<MatchId, GameInfo>> fetchAll() {
        return CompletableFuture.supplyAsync(database::getAll);
    }

}
