package dev.luzifer.data.access;

import dev.luzifer.data.access.shit.ChampInfo;
import dev.luzifer.data.access.shit.DeckInfo;
import dev.luzifer.data.access.shit.GameInfo;
import dev.luzifer.data.access.shit.ItemInfo;
import dev.luzifer.data.access.shit.PlayerInfo;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.dto.GameDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("gameDao")
public class GameDao {

    private final Map<String, Integer> mapCache = new HashMap<>();
    private final Map<String, Integer> regionCache = new HashMap<>();
    
    private final Database database = new Database();

    public void initializeDatabase() {
        database.initialize();
    }

    public void saveGameData(GameDto gameDto) {

        GameInfo gameInfo = new GameInfo(
                gameDto.getId(),
                gameDto.getRanked(),
                gameDto.getAverageRank(),
                convertMapNameToId(gameDto.getMapName()),
                gameDto.getBannedChamps(),
                gameDto.getTeam1Points(),
                gameDto.getTeam2Points(),
                gameDto.getDuration(),
                gameDto.getTimestamp(),
                gameDto.getSeason());

        database.insertGameInfo(gameInfo);
        database.insertBannedChamps(gameInfo);

        ChampInfo[] champInfos = new ChampInfo[gameDto.getChamps().length];
        PlayerInfo[] playerInfos = new PlayerInfo[gameDto.getChamps().length];
        DeckInfo[] deckInfos = new DeckInfo[gameDto.getChamps().length];
        ItemInfo[] itemInfos = new ItemInfo[gameDto.getChamps().length];

        int index = 0;
        for(ChampDto champDto : gameDto.getChamps()) {
            ChampInfo champInfo = new ChampInfo(champDto.getChampId(),
                    champDto.getLeagueTier(),
                    champDto.getLeaguePoints(),
                    champDto.getChampLevel(),
                    champDto.getWon(),
                    champDto.getCategoryId(),
                    champDto.getGoldEarned(),
                    champDto.getKillingSpree(),
                    champDto.getKills(),
                    champDto.getDeaths(),
                    champDto.getAssists(),
                    champDto.getDamageDone(),
                    champDto.getDamageTaken(),
                    champDto.getDamageShielded(),
                    champDto.getHeal(),
                    champDto.getSelfHeal(),
                    gameDto.getId(),
                    champDto.getPlayerId());
            champInfos[index] = champInfo;

            PlayerInfo playerInfo = new PlayerInfo(champDto.getPlayerId(),
                    champDto.getPlayerName(),
                    convertRegionNameToId(champDto.getRegion()),
                    champDto.getPlatformId());
            playerInfos[index] = playerInfo;
            
            DeckInfo deckInfo = new DeckInfo(champDto.getTalentId(),
                    champDto.getDeckCard1(),
                    champDto.getDeckCard2(),
                    champDto.getDeckCard3(),
                    champDto.getDeckCard4(),
                    champDto.getDeckCard5(),
                    champDto.getDeckCard1Level(),
                    champDto.getDeckCard2Level(),
                    champDto.getDeckCard3Level(),
                    champDto.getDeckCard4Level(),
                    champDto.getDeckCard5Level(),
                    champDto.getWon(),
                    gameDto.getId(),
                    champDto.getChampId());
            deckInfos[index] = deckInfo;
            
            ItemInfo itemInfo = new ItemInfo(champDto.getItem1(),
                    champDto.getItem2(),
                    champDto.getItem3(),
                    champDto.getItem4(),
                    champDto.getItem1Level(),
                    champDto.getItem2Level(),
                    champDto.getItem3Level(),
                    champDto.getItem4Level(),
                    champDto.getWon(),
                    gameDto.getId(),
                    champDto.getChampId());
            itemInfos[index] = itemInfo;
            index++;
        }

        database.insertBatchChampInfos(champInfos);
        database.insertBatchPlayerInfos(playerInfos);
        database.insertBatchDeckInfos(deckInfos);
        database.insertBatchItemInfos(itemInfos);
    }

    public int getTotalGameCount() {
        return database.getTotalGames();
    }

    private int convertMapNameToId(String mapName) {
        int id = mapCache.getOrDefault(mapName, database.getIdForMap(mapName));
        if(id == -1) {
            database.insertMapInfo(mapName);
            id = database.getIdForMap(mapName);
            mapCache.put(mapName, id);
        }

        return id;
    }
    
    private int convertRegionNameToId(String regionName) {
        int id = regionCache.getOrDefault(regionName, database.getIdForRegion(regionName));
        if(id == -1) {
            database.insertRegionInfo(regionName);
            id = database.getIdForRegion(regionName);
            regionCache.put(regionName, id);
        }
        
        return id;
    }
}
