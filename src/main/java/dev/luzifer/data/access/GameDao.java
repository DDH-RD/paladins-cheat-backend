package dev.luzifer.data.access;

import dev.luzifer.Main;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import org.springframework.stereotype.Component;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void insert(GameDto[] gameDtos) {
        database.insert(gameDtos);
        Main.LOGGER.info("INSERTED " + gameDtos.length + " GAMES");
    }

    public int count() {
        int count = database.countEntries();
        Main.LOGGER.info("COUNTED " + count + " GAMES");
        return count;
    }

    public GameDto[] fetchMatches() {
        GameDto[] matches = database.fetchAll();
        Main.LOGGER.info("FETCHED " + matches.length + " GAMES");
        return matches;
    }

    public ChampDto[] fetchChampsForMatch(int matchId) {
        ChampDto[] champs = database.fetchChamps(matchId);
        Main.LOGGER.info("FETCHED " + champs.length + " CHAMPS FOR MATCH " + matchId);
        return champs;
    }
}
