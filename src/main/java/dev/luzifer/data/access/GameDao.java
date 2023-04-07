package dev.luzifer.data.access;

import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import dev.luzifer.spring.controller.GameController;
import org.springframework.stereotype.Component;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void insert(GameDto[] gameDtos) {
        database.insert(gameDtos);
    }

    public int count(GameController.MatchType matchType) {
        return database.countEntries(matchType);
    }

    public GameDto[] fetchMatches(GameController.MatchType matchType) {
        return database.fetchGames(matchType);
    }

    public GameDto[] fetchMatchesOnMap(GameController.MatchType matchType, String mapName) {
        return database.fetchGamesOnMap(matchType, mapName);
    }

    public GameDto[] fetchMatchesWithChamp(GameController.MatchType matchType, int champId) {
        return database.fetchGamesWithChamp(matchType, champId);
    }

    public ChampDto[] fetchChampsForMatch(int matchId) {
        return database.fetchChamps(matchId);
    }
}
