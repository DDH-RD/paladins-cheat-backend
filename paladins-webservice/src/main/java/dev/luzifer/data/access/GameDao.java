package dev.luzifer.data.access;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.shit.ChampInfo;
import dev.luzifer.data.access.shit.DeckInfo;
import dev.luzifer.data.access.shit.GameInfo;
import dev.luzifer.data.access.shit.ItemInfo;
import dev.luzifer.data.access.shit.PlayerInfo;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.dto.GameDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("gameDao")
public class GameDao {

    private final Map<String, Integer> mapCache = new HashMap<>();
    private final Map<String, Integer> regionCache = new HashMap<>();

    private final Database database = new Database();

    public void initializeDatabase() {
        database.initialize();
    }

    public DatabaseResult<Void> saveGameData(GameDto gameDto) {

        GameInfo gameInfo = getGameInfo(gameDto);
        DatabaseResult<Void> gameInfoResult = database.insertGameInfo(gameInfo);

        if(gameInfoResult.getDatabaseResultType() != DatabaseResult.DatabaseResultType.SUCCESS) {
            return gameInfoResult;
        }

        ChampInfo[] champInfos = new ChampInfo[gameDto.getChamps().length];
        PlayerInfo[] playerInfos = new PlayerInfo[gameDto.getChamps().length];
        DeckInfo[] deckInfos = new DeckInfo[gameDto.getChamps().length];
        ItemInfo[] itemInfos = new ItemInfo[gameDto.getChamps().length];

        fillInfoArrays(gameDto, champInfos, playerInfos, deckInfos, itemInfos);
        insertInfosToDatabase(gameInfo, champInfos, playerInfos, deckInfos, itemInfos);

        return gameInfoResult;
    }

    public long getLatestMatchId() {
        return database.getLatestMatchId().getResult().orElse(-1L);
    }

    public int getTotalGameCount() {
        return database.getTotalGames().getResult().orElse(-1);
    }

    public int getTotalChampCount() {
        return database.getTotalChamps().getResult().orElse(-1);
    }

    public int getTotalPlayerCount() {
        return database.getTotalPlayers().getResult().orElse(-1);
    }

    public int getTotalDeckCount() {
        return database.getTotalDecks().getResult().orElse(-1);
    }

    public int getTotalItemCount() {
        return database.getTotalItemCrafts().getResult().orElse(-1);
    }

    public int getTotalBannedChampCount() {
        return database.getTotalBannedChamps().getResult().orElse(-1);
    }

    public int getTotalMapCount() {
        return database.getTotalMaps().getResult().orElse(-1);
    }

    public int getTotalRegionCount() {
        return database.getTotalRegions().getResult().orElse(-1);
    }

    public List<Integer> getBans() {
        return database.getBans().getResult().orElse(Collections.emptyList());
    }

    public List<Integer> getBansForMap(int mapId) {
        return database.getBansForMap(mapId).getResult().orElse(Collections.emptyList());
    }

    public List<Integer> getBansForCategory(int categoryId) {
        return database.getBansForCategory(categoryId).getResult().orElse(Collections.emptyList());
    }

    public List<Integer> getBansForCategoryOnMap(int categoryId, int mapId) {
        return database.getBansForCategoryOnMap(categoryId, mapId).getResult().orElse(Collections.emptyList());
    }

    private void fillInfoArrays(GameDto gameDto, ChampInfo[] champInfos, PlayerInfo[] playerInfos, DeckInfo[] deckInfos, ItemInfo[] itemInfos) {
        int index = 0;
        for(ChampDto champDto : gameDto.getChamps()) {
            ChampInfo champInfo = getChampInfo(gameDto, champDto);
            champInfos[index] = champInfo;

            PlayerInfo playerInfo = getPlayerInfo(champDto);
            playerInfos[index] = playerInfo;

            DeckInfo deckInfo = getDeckInfo(gameDto, champDto);
            deckInfos[index] = deckInfo;

            ItemInfo itemInfo = getItemInfo(gameDto, champDto);
            itemInfos[index] = itemInfo;
            index++;
        }
    }

    private void insertInfosToDatabase(GameInfo gameInfo, ChampInfo[] champInfos, PlayerInfo[] playerInfos, DeckInfo[] deckInfos, ItemInfo[] itemInfos) {
        database.insertBannedChamps(gameInfo);
        database.insertBatchPlayerInfos(playerInfos);
        database.insertBatchChampInfos(champInfos);
        database.insertBatchDeckInfos(deckInfos);
        database.insertBatchItemInfos(itemInfos);
    }

    private GameInfo getGameInfo(GameDto gameDto) {
        return new GameInfo(
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
    }

    private ChampInfo getChampInfo(GameDto gameDto, ChampDto champDto) {
        return new ChampInfo(champDto.getChampId(),
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
    }

    private PlayerInfo getPlayerInfo(ChampDto champDto) {
        return new PlayerInfo(champDto.getPlayerId(),
                champDto.getPlayerName(),
                convertRegionNameToId(champDto.getRegion()),
                champDto.getPlatformId());
    }

    private DeckInfo getDeckInfo(GameDto gameDto, ChampDto champDto) {
        return new DeckInfo(champDto.getTalentId(),
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
                gameDto.getId(),
                champDto.getChampId());
    }

    private ItemInfo getItemInfo(GameDto gameDto, ChampDto champDto) {
        return new ItemInfo(champDto.getItem1(),
                champDto.getItem2(),
                champDto.getItem3(),
                champDto.getItem4(),
                champDto.getItem1Level(),
                champDto.getItem2Level(),
                champDto.getItem3Level(),
                champDto.getItem4Level(),
                gameDto.getId(),
                champDto.getChampId());
    }

    public int convertMapNameToId(String mapName) {
        if (mapCache.containsKey(mapName)) {
            return mapCache.get(mapName);
        }

        int mapId = database.getIdForMap(mapName).getResult().orElse(-1);

        if (mapId == -1) {
            Webservice.DATABASE_LOGGER.info("Could not find map id! Inserting new map.. [" + mapName + "]");
            database.insertMapInfo(mapName);

            return convertMapNameToId(mapName);
        }

        mapCache.put(mapName, mapId);
        return mapId;
    }

    public int convertRegionNameToId(String regionName) {
        if (regionCache.containsKey(regionName)) {
            return regionCache.get(regionName);
        }

        int regionId = database.getIdForRegion(regionName).getResult().orElse(-1);

        if (regionId == -1) {
            Webservice.DATABASE_LOGGER.info("Could not find region id! Inserting new region.. [" + regionName + "]");
            database.insertRegionInfo(regionName);

            return convertRegionNameToId(regionName);
        }

        regionCache.put(regionName, regionId);
        return regionId;
    }
}
