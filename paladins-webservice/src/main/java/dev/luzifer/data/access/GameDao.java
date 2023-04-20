package dev.luzifer.data.access;

import dev.luzifer.Webservice;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();
    private final DatabaseCache cache = new DatabaseCache();
    
    {
        cache.init(database.fetchAllChampData(null));
        Webservice.DATABASE_LOGGER.info("Database cache initialized");
    }

    public void insertChampData(ChampData[] champData) {
        database.insertChampData(champData);
        Arrays.stream(champData).forEach(cache::update);
    }

    public int count(Double season) {
        return cache.getChampDataSet().size();
    }

    public Set<ChampData> fetchAllChampData(Double season) {
        return cache.getChampDataSet();
    }

    public Set<ChampData> fetchChampDataForMatch(int matchId) {
        return cache.getChampDataForMatch(matchId);
    }

    public Set<ChampData> fetchChampDataForMatchOfCategory(int matchId, int categoryId) {
        return cache.getChampDataForMatchOfCategory(matchId, categoryId);
    }

    public Set<ChampData> fetchChampDataForChamp(Double season, int champId) {
        return cache.getChampDataForChamp(champId);
    }

    public Set<ChampData> fetchChampDataForMap(Double season, String mapName) {
        return cache.getChampDataForMap(mapName);
    }

    public Set<ChampData> fetchChampDataForCategory(Double season, int categoryId) {
        return cache.getChampDataForCategory(categoryId);
    }

    public Set<ChampData> fetchChampDataForMapOfCategory(Double season, String mapName, int categoryId) {
        return cache.getChampDataForMapOfCategory(mapName, categoryId);
    }
}
