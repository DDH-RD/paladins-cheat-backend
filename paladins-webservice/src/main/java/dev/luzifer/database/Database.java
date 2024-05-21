package dev.luzifer.database;

import dev.luzifer.database.converter.EntityConverter;
import dev.luzifer.database.dto.ChampDto;
import dev.luzifer.database.dto.GameDto;
import dev.luzifer.database.objects.PlayedChamp;
import dev.luzifer.database.objects.Deck;
import dev.luzifer.database.objects.ItemDraft;
import dev.luzifer.database.objects.Map;
import dev.luzifer.database.objects.PaladinsChamp;
import dev.luzifer.database.objects.Player;
import dev.luzifer.database.objects.Region;
import dev.luzifer.database.objects.flaws.Match;
import dev.luzifer.database.repositories.PlayedChampRepository;
import dev.luzifer.database.repositories.DeckRepository;
import dev.luzifer.database.repositories.MatchRepository;
import dev.luzifer.database.repositories.ItemDraftRepository;
import dev.luzifer.database.repositories.MapRepository;
import dev.luzifer.database.repositories.PaladinsChampRepository;
import dev.luzifer.database.repositories.PlayerRepository;
import dev.luzifer.database.repositories.RegionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Database {

  private final MatchRepository matchRepository;
  private final PlayedChampRepository champInfoRepository;
  private final ItemDraftRepository itemDraftRepository;
  private final DeckRepository deckRepository;
  private final MapRepository mapRepository;
  private final PlayerRepository playerRepository;
  private final RegionRepository regionRepository;
  private final PaladinsChampRepository paladinsChampRepository;
  private final EntityConverter entityConverter;

  @Autowired
  public Database(
      MatchRepository matchRepository,
      PlayedChampRepository champInfoRepository,
      ItemDraftRepository itemDraftRepository,
      DeckRepository deckRepository,
      MapRepository mapRepository,
      PlayerRepository playerRepository,
      RegionRepository regionRepository,
      PaladinsChampRepository paladinsChampRepository,
      EntityConverter entityConverter) {
    this.matchRepository = matchRepository;
    this.champInfoRepository = champInfoRepository;
    this.itemDraftRepository = itemDraftRepository;
    this.deckRepository = deckRepository;
    this.mapRepository = mapRepository;
    this.playerRepository = playerRepository;
    this.regionRepository = regionRepository;
    this.paladinsChampRepository = paladinsChampRepository;
    this.entityConverter = entityConverter;
  }

  public void saveGames(GameDto[] gameDtos) {
    List<Match> matches = new ArrayList<>();
    List<Map> maps = new ArrayList<>();
    List<PlayedChamp> playedChamps = new ArrayList<>();
    List<ItemDraft> itemDrafts = new ArrayList<>();
    List<Deck> decks = new ArrayList<>();
    List<Player> players = new ArrayList<>();
    List<Region> regions = new ArrayList<>();
    List<PaladinsChamp> paladinsChamps = new ArrayList<>();

    for (GameDto gameDto : gameDtos) {
      matches.add(entityConverter.convertToGameInfo(gameDto));
      maps.add(entityConverter.convertToMapInfo(gameDto.getMapName()));

      for (ChampDto champDto : gameDto.getChamps()) {
        playedChamps.add(entityConverter.convertToChampInfo(champDto));
        itemDrafts.add(entityConverter.convertToItemInfo(champDto));
        decks.add(entityConverter.convertToDeckInfo(champDto));
        players.add(entityConverter.convertToPlayerInfo(champDto));
        regions.add(entityConverter.convertToRegionInfo(champDto.getRegion()));
        paladinsChamps.add(entityConverter.convertToPaladinsChampInfo(champDto));
      }
    }

    matchRepository.saveAll(matches);
    mapRepository.saveAll(maps);
    champInfoRepository.saveAll(playedChamps);
    itemDraftRepository.saveAll(itemDrafts);
    deckRepository.saveAll(decks);
    playerRepository.saveAll(players);
    regionRepository.saveAll(regions);
    paladinsChampRepository.saveAll(paladinsChamps);
  }

  public long countGameInfos() {
    return matchRepository.count();
  }
}
