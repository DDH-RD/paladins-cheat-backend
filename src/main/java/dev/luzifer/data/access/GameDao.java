package dev.luzifer.data.access;

import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import org.springframework.stereotype.Component;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void insert(GameDto[] gameDtos) {
        database.insert(gameDtos);
    }

    public int count(boolean ranked) {
        return database.countEntries(ranked);
    }

    public GameDto[] fetchMatches(boolean ranked) {
        return database.fetchGames(ranked);
    }

    public ChampDto[] fetchChampsForMatch(int matchId) {
        return database.fetchChamps(matchId);
    }
}
