package dev.luzifer.database.converter;

import dev.luzifer.database.dto.BannedChampDto;
import dev.luzifer.database.dto.ChampDto;
import dev.luzifer.database.dto.GameDto;
import dev.luzifer.database.objects.BannedChamp;
import dev.luzifer.database.objects.Deck;
import dev.luzifer.database.objects.ItemDraft;
import dev.luzifer.database.objects.Map;
import dev.luzifer.database.objects.PaladinsChamp;
import dev.luzifer.database.objects.PlayedChamp;
import dev.luzifer.database.objects.Player;
import dev.luzifer.database.objects.Region;
import dev.luzifer.database.objects.Match;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EntityConverter {

  public Match convertToGameInfo(GameDto gameDto) {
    Match match = new Match();
    match.setAverageRank(gameDto.getAverageRank());
    match.setMap(convertToMapInfo(gameDto.getMapName()));
    match.setRanked(gameDto.getRanked());
    match.setDuration(gameDto.getDuration());
    match.setSeason(gameDto.getSeason());
    match.setMatchId(gameDto.getId());
    match.setBannedChamps(convertBannedChamps(gameDto));
    match.setTeam1Points(gameDto.getTeam1Points());
    match.setTeam2Points(gameDto.getTeam2Points());
    return match;
  }

  public List<BannedChamp> convertBannedChamps(GameDto gameDto) {
    List<BannedChamp> bannedChamps = new ArrayList<>();
    Match match = convertToGameInfo(gameDto);
    for (BannedChampDto bannedChampDto : gameDto.getBannedChamps()) {
      BannedChamp bannedChamp = new BannedChamp();
      bannedChamp.setMatch(match);
      bannedChamp.setChamp(convertToPaladinsChampFromBannedChampDto(bannedChampDto));
      bannedChamps.add(bannedChamp);
    }
    return bannedChamps;
  }

  private PaladinsChamp convertToPaladinsChampFromBannedChampDto(BannedChampDto bannedChampDto) {
    PaladinsChamp paladinsChamp = new PaladinsChamp();
    paladinsChamp.setId(bannedChampDto.getChampId());
    paladinsChamp.setCategoryId(bannedChampDto.getCategoryId());
    return paladinsChamp;
  }

  public PlayedChamp convertToChampInfo(ChampDto champDto) {
    PlayedChamp playedChamp = new PlayedChamp();
    playedChamp.setChampInfo(convertToPaladinsChampInfo(champDto));
    playedChamp.setChampLevel(champDto.getChampLevel());
    playedChamp.setLeagueTier(champDto.getLeagueTier());
    playedChamp.setLeaguePoints(champDto.getLeaguePoints());
    playedChamp.setPlayer(convertToPlayerInfo(champDto));
    playedChamp.setWon(champDto.getWon());
    playedChamp.setSelfHeal(champDto.getSelfHeal());
    playedChamp.setHeal(champDto.getHeal());
    playedChamp.setDamageDone(champDto.getDamageDone());
    playedChamp.setDamageTaken(champDto.getDamageTaken());
    playedChamp.setDamageShielded(champDto.getDamageShielded());
    playedChamp.setDeaths(champDto.getDeaths());
    playedChamp.setKills(champDto.getKills());
    playedChamp.setKillingSpree(champDto.getKillingSpree());
    playedChamp.setAssists(champDto.getAssists());
    playedChamp.setGoldEarned(champDto.getGoldEarned());
    return playedChamp;
  }

  public PaladinsChamp convertToPaladinsChampInfo(ChampDto champDto) {
    PaladinsChamp paladinsChamp = new PaladinsChamp();
    paladinsChamp.setId(champDto.getChampId());
    paladinsChamp.setCategoryId(champDto.getCategoryId());
    return paladinsChamp;
  }

  public Map convertToMapInfo(String mapName) {
    Map map = new Map();
    map.setMapName(mapName);
    return map;
  }

  public Player convertToPlayerInfo(ChampDto champDto) {
    Player player = new Player();
    player.setPlayerName(champDto.getPlayerName());
    player.setPlayerId(champDto.getPlayerId());
    player.setPlatformId(champDto.getPlatformId());
    player.setRegion(convertToRegionInfo(champDto.getRegion()));
    return player;
  }

  public Region convertToRegionInfo(String regionName) {
    Region region = new Region();
    region.setRegionName(regionName);
    return region;
  }

  public ItemDraft convertToItemInfo(ChampDto champDto) {
    ItemDraft itemDraft = new ItemDraft();
    itemDraft.setItem1(champDto.getItem1());
    itemDraft.setItem2(champDto.getItem2());
    itemDraft.setItem3(champDto.getItem3());
    itemDraft.setItem4(champDto.getItem4());
    itemDraft.setItem1Level(champDto.getItem1Level());
    itemDraft.setItem2Level(champDto.getItem2Level());
    itemDraft.setItem3Level(champDto.getItem3Level());
    itemDraft.setItem4Level(champDto.getItem4Level());
    itemDraft.setChamp(convertToChampInfo(champDto));
    return itemDraft;
  }

  public Deck convertToDeckInfo(ChampDto champDto) {
    Deck deck = new Deck();
    deck.setTalentId(champDto.getTalentId());
    deck.setDeckCard1(champDto.getDeckCard1());
    deck.setDeckCard2(champDto.getDeckCard2());
    deck.setDeckCard3(champDto.getDeckCard3());
    deck.setDeckCard4(champDto.getDeckCard4());
    deck.setDeckCard5(champDto.getDeckCard5());
    deck.setDeckCard1Level(champDto.getDeckCard1Level());
    deck.setDeckCard2Level(champDto.getDeckCard2Level());
    deck.setDeckCard3Level(champDto.getDeckCard3Level());
    deck.setDeckCard4Level(champDto.getDeckCard4Level());
    deck.setDeckCard5Level(champDto.getDeckCard5Level());
    deck.setChamp(convertToChampInfo(champDto));
    return deck;
  }
}
