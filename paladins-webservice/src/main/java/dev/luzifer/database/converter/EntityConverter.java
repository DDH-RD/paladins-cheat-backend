package dev.luzifer.database.converter;

import dev.luzifer.database.dto.ChampDto;
import dev.luzifer.database.dto.GameDto;
import dev.luzifer.database.objects.ChampInfo;
import dev.luzifer.database.objects.DeckInfo;
import dev.luzifer.database.objects.ItemInfo;
import dev.luzifer.database.objects.MapInfo;
import dev.luzifer.database.objects.PlayerInfo;
import dev.luzifer.database.objects.RegionInfo;
import dev.luzifer.database.objects.flaws.GameInfo;
import org.springframework.stereotype.Component;

@Component
public class EntityConverter {

  public GameInfo convertToGameInfo(GameDto gameDto) {
    GameInfo gameInfo = new GameInfo();
    gameInfo.setAverageRank(gameDto.getAverageRank());
    gameInfo.setMap(convertToMapInfo(gameDto.getMapName()));
    gameInfo.setRanked(gameDto.getRanked());
    gameInfo.setDuration(gameDto.getDuration());
    gameInfo.setSeason(gameDto.getSeason());
    gameInfo.setMatchId(gameDto.getId());
    gameInfo.setBannedChamps(gameDto.getBannedChamps());
    gameInfo.setTeam1Points(gameDto.getTeam1Points());
    gameInfo.setTeam2Points(gameDto.getTeam2Points());
    return gameInfo;
  }

  public ChampInfo convertToChampInfo(ChampDto champDto) {
    ChampInfo champInfo = new ChampInfo();
    champInfo.setChampId(champDto.getChampId());
    champInfo.setChampLevel(champDto.getChampLevel());
    champInfo.setLeagueTier(champDto.getLeagueTier());
    champInfo.setLeaguePoints(champDto.getLeaguePoints());
    champInfo.setPlayer(convertToPlayerInfo(champDto));
    champInfo.setCategoryId(champDto.getCategoryId());
    champInfo.setWon(champDto.getWon());
    champInfo.setSelfHeal(champDto.getSelfHeal());
    champInfo.setHeal(champDto.getHeal());
    champInfo.setDamageDone(champDto.getDamageDone());
    champInfo.setDamageTaken(champDto.getDamageTaken());
    champInfo.setDamageShielded(champDto.getDamageShielded());
    champInfo.setDeaths(champDto.getDeaths());
    champInfo.setKills(champDto.getKills());
    champInfo.setKillingSpree(champDto.getKillingSpree());
    champInfo.setAssists(champDto.getAssists());
    champInfo.setGoldEarned(champDto.getGoldEarned());
    return champInfo;
  }

  public MapInfo convertToMapInfo(String mapName) {
    MapInfo mapInfo = new MapInfo();
    mapInfo.setMapName(mapName);
    return mapInfo;
  }

  public PlayerInfo convertToPlayerInfo(ChampDto champDto) {
    PlayerInfo playerInfo = new PlayerInfo();
    playerInfo.setPlayerName(champDto.getPlayerName());
    playerInfo.setPlayerId(champDto.getPlayerId());
    playerInfo.setPlatformId(champDto.getPlatformId());
    playerInfo.setRegion(convertToRegionInfo(champDto.getRegion()));
    return playerInfo;
  }

  public RegionInfo convertToRegionInfo(String regionName) {
    RegionInfo regionInfo = new RegionInfo();
    regionInfo.setRegionName(regionName);
    return regionInfo;
  }

  public ItemInfo convertToItemInfo(ChampDto champDto) {
    ItemInfo itemInfo = new ItemInfo();
    itemInfo.setItem1(champDto.getItem1());
    itemInfo.setItem2(champDto.getItem2());
    itemInfo.setItem3(champDto.getItem3());
    itemInfo.setItem4(champDto.getItem4());
    itemInfo.setItem1Level(champDto.getItem1Level());
    itemInfo.setItem2Level(champDto.getItem2Level());
    itemInfo.setItem3Level(champDto.getItem3Level());
    itemInfo.setItem4Level(champDto.getItem4Level());
    itemInfo.setChamp(convertToChampInfo(champDto));
    return itemInfo;
  }

  public DeckInfo convertToDeckInfo(ChampDto champDto) {
    DeckInfo deckInfo = new DeckInfo();
    deckInfo.setTalentId(champDto.getTalentId());
    deckInfo.setDeckCard1(champDto.getDeckCard1());
    deckInfo.setDeckCard2(champDto.getDeckCard2());
    deckInfo.setDeckCard3(champDto.getDeckCard3());
    deckInfo.setDeckCard4(champDto.getDeckCard4());
    deckInfo.setDeckCard5(champDto.getDeckCard5());
    deckInfo.setDeckCard1Level(champDto.getDeckCard1Level());
    deckInfo.setDeckCard2Level(champDto.getDeckCard2Level());
    deckInfo.setDeckCard3Level(champDto.getDeckCard3Level());
    deckInfo.setDeckCard4Level(champDto.getDeckCard4Level());
    deckInfo.setDeckCard5Level(champDto.getDeckCard5Level());
    deckInfo.setChamp(convertToChampInfo(champDto));
    return deckInfo;
  }
}
