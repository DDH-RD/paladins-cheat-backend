package dev.luzifer.database.converter;

import dev.luzifer.database.dto.ChampDto;
import dev.luzifer.database.dto.GameDto;
import dev.luzifer.database.objects.DeckInfo;
import dev.luzifer.database.objects.ItemInfo;
import dev.luzifer.database.objects.MapInfo;
import dev.luzifer.database.objects.PaladinsChampInfo;
import dev.luzifer.database.objects.PlayedChampInfo;
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

  public PlayedChampInfo convertToChampInfo(ChampDto champDto) {
    PlayedChampInfo playedChampInfo = new PlayedChampInfo();
    playedChampInfo.setChampId(champDto.getChampId());
    playedChampInfo.setChampLevel(champDto.getChampLevel());
    playedChampInfo.setLeagueTier(champDto.getLeagueTier());
    playedChampInfo.setLeaguePoints(champDto.getLeaguePoints());
    playedChampInfo.setPlayer(convertToPlayerInfo(champDto));
    playedChampInfo.setWon(champDto.getWon());
    playedChampInfo.setSelfHeal(champDto.getSelfHeal());
    playedChampInfo.setHeal(champDto.getHeal());
    playedChampInfo.setDamageDone(champDto.getDamageDone());
    playedChampInfo.setDamageTaken(champDto.getDamageTaken());
    playedChampInfo.setDamageShielded(champDto.getDamageShielded());
    playedChampInfo.setDeaths(champDto.getDeaths());
    playedChampInfo.setKills(champDto.getKills());
    playedChampInfo.setKillingSpree(champDto.getKillingSpree());
    playedChampInfo.setAssists(champDto.getAssists());
    playedChampInfo.setGoldEarned(champDto.getGoldEarned());
    return playedChampInfo;
  }

  public PaladinsChampInfo convertToPaladinsChampInfo(ChampDto champDto) {
    PaladinsChampInfo paladinsChampInfo = new PaladinsChampInfo();
    paladinsChampInfo.setId(champDto.getChampId());
    paladinsChampInfo.setCategoryId(champDto.getCategoryId());
    return paladinsChampInfo;
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
