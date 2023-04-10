package dev.luzifer.data.access;

import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void insertChampData(ChampData[] champData) {
        database.insertChampData(champData);
    }

    public int count(GameController.MatchType matchType) {
        return database.countEntries(matchType);
    }

    public List<ChampData> fetchAllChampData(GameController.MatchType matchType) {
        return database.fetchAllChampData(matchType);
    }

    public List<ChampData> fetchChampDataForMatch(GameController.MatchType matchType, int matchId) {
        return database.fetchChampDataForMatch(matchType, matchId);
    }

    public List<ChampData> fetchChampDataForMatchOfCategory(GameController.MatchType matchType, int matchId, int categoryId) {
        return database.fetchChampDataForMatchOfCategory(matchType, matchId, categoryId);
    }

    public List<ChampData> fetchChampDataForChamp(GameController.MatchType matchType, int champId) {
        return database.fetchChampDataForChamp(matchType, champId);
    }

    public List<ChampData> fetchChampDataForMap(GameController.MatchType matchType, String mapName) {
        return database.fetchChampDataForMap(matchType, mapName);
    }

    public List<ChampData> fetchChampDataForCategory(GameController.MatchType matchType, int categoryId) {
        return database.fetchChampDataForCategory(matchType, categoryId);
    }

    public List<ChampData> fetchChampDataForMapOfCategory(GameController.MatchType matchType, String mapName, int categoryId) {
        return database.fetchChampDataForMapOfCategory(matchType, mapName, categoryId);
    }
}
