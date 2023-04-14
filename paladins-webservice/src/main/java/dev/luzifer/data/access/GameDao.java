package dev.luzifer.data.access;

import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void insertChampData(ChampData[] champData) {
        database.insertChampData(champData);
    }

    public int count(double season) {
        return database.countEntries(season);
    }

    public List<ChampData> fetchAllChampData(double season) {
        return database.fetchAllChampData(season);
    }

    public List<ChampData> fetchChampDataForMatch(int matchId) {
        return database.fetchChampDataForMatch(matchId);
    }

    public List<ChampData> fetchChampDataForMatchOfCategory(int matchId, int categoryId) {
        return database.fetchChampDataForMatchOfCategory(matchId, categoryId);
    }

    public List<ChampData> fetchChampDataForChamp(double season, int champId) {
        return database.fetchChampDataForChamp(season, champId);
    }

    public List<ChampData> fetchChampDataForMap(double season, String mapName) {
        return database.fetchChampDataForMap(season, mapName);
    }

    public List<ChampData> fetchChampDataForCategory(double season, int categoryId) {
        return database.fetchChampDataForCategory(season, categoryId);
    }

    public List<ChampData> fetchChampDataForMapOfCategory(double season, String mapName, int categoryId) {
        return database.fetchChampDataForMapOfCategory(season, mapName, categoryId);
    }
}
