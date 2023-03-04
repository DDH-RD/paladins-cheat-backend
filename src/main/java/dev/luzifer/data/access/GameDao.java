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

    public int count() {
        return database.countEntries();
    }

    public GameDto[] fetchMatches() {
        return database.fetchAll();
    }

    public ChampDto[] fetchChampsForMatch(int matchId) {
        return database.fetchChamps(matchId);
    }
}
