package dev.luzifer.data.access;

import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.json.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("matchDao")
public class MatchDao {

    private final Database database = new Database();

    public void delete(MatchId matchId) {
        database.delete(matchId.getId());
    }

    public void save(MatchId matchId, GameInfo gameInfo) {
        database.insert(matchId.getId(), JsonUtil.toJson(gameInfo));
    }

    public void saveMultiple(Map<MatchId, GameInfo> map) {
        database.insertMultiple(map);
    }

    public Map<MatchId, GameInfo> fetchAll() {
        return database.getAll();
    }

}
