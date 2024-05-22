package dev.luzifer.data;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.data.entity.PlayedChamp;
import dev.luzifer.data.entity.Deck;
import dev.luzifer.data.entity.ItemDraft;
import dev.luzifer.data.entity.Map;
import dev.luzifer.data.entity.PaladinsChamp;
import dev.luzifer.data.entity.Player;
import dev.luzifer.data.entity.Region;
import dev.luzifer.data.entity.Match;
import dev.luzifer.data.repository.PlayedChampRepository;
import dev.luzifer.data.repository.DeckRepository;
import dev.luzifer.data.repository.MatchRepository;
import dev.luzifer.data.repository.ItemDraftRepository;
import dev.luzifer.data.repository.MapRepository;
import dev.luzifer.data.repository.PaladinsChampRepository;
import dev.luzifer.data.repository.PlayerRepository;
import dev.luzifer.data.repository.RegionRepository;
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
