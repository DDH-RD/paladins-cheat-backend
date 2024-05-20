package dev.luzifer.database;

import dev.luzifer.database.converter.EntityConverter;
import dev.luzifer.database.dto.ChampDto;
import dev.luzifer.database.dto.GameDto;
import dev.luzifer.database.objects.ChampInfo;
import dev.luzifer.database.objects.DeckInfo;
import dev.luzifer.database.objects.ItemInfo;
import dev.luzifer.database.objects.MapInfo;
import dev.luzifer.database.objects.PlayerInfo;
import dev.luzifer.database.objects.RegionInfo;
import dev.luzifer.database.objects.flaws.GameInfo;
import dev.luzifer.database.repositories.ChampInfoRepository;
import dev.luzifer.database.repositories.DeckInfoRepository;
import dev.luzifer.database.repositories.GameInfoRepository;
import dev.luzifer.database.repositories.ItemInfoRepository;
import dev.luzifer.database.repositories.MapInfoRepository;
import dev.luzifer.database.repositories.PlayerInfoRepository;
import dev.luzifer.database.repositories.RegionInfoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Database {

  private final GameInfoRepository gameInfoRepository;
  private final ChampInfoRepository champInfoRepository;
  private final ItemInfoRepository itemInfoRepository;
  private final DeckInfoRepository deckInfoRepository;
  private final MapInfoRepository mapInfoRepository;
  private final PlayerInfoRepository playerInfoRepository;
  private final RegionInfoRepository regionInfoRepository;
  private final EntityConverter entityConverter;

  @Autowired
  public Database(
      GameInfoRepository gameInfoRepository,
      ChampInfoRepository champInfoRepository,
      ItemInfoRepository itemInfoRepository,
      DeckInfoRepository deckInfoRepository,
      MapInfoRepository mapInfoRepository,
      PlayerInfoRepository playerInfoRepository,
      RegionInfoRepository regionInfoRepository,
      EntityConverter entityConverter) {
    this.gameInfoRepository = gameInfoRepository;
    this.champInfoRepository = champInfoRepository;
    this.itemInfoRepository = itemInfoRepository;
    this.deckInfoRepository = deckInfoRepository;
    this.mapInfoRepository = mapInfoRepository;
    this.playerInfoRepository = playerInfoRepository;
    this.regionInfoRepository = regionInfoRepository;
    this.entityConverter = entityConverter;
  }

  public void saveGames(GameDto[] gameDtos) {
    List<GameInfo> gameInfos = new ArrayList<>();
    List<MapInfo> mapInfos = new ArrayList<>();
    List<ChampInfo> champInfos = new ArrayList<>();
    List<ItemInfo> itemInfos = new ArrayList<>();
    List<DeckInfo> deckInfos = new ArrayList<>();
    List<PlayerInfo> playerInfos = new ArrayList<>();
    List<RegionInfo> regionInfos = new ArrayList<>();

    for (GameDto gameDto : gameDtos) {
      gameInfos.add(entityConverter.convertToGameInfo(gameDto));
      mapInfos.add(entityConverter.convertToMapInfo(gameDto.getMapName()));

      for (ChampDto champDto : gameDto.getChamps()) {
        champInfos.add(entityConverter.convertToChampInfo(champDto));
        itemInfos.add(entityConverter.convertToItemInfo(champDto));
        deckInfos.add(entityConverter.convertToDeckInfo(champDto));
        playerInfos.add(entityConverter.convertToPlayerInfo(champDto));
        regionInfos.add(entityConverter.convertToRegionInfo(champDto.getRegion()));
      }
    }

    gameInfoRepository.saveAll(gameInfos);
    mapInfoRepository.saveAll(mapInfos);
    champInfoRepository.saveAll(champInfos);
    itemInfoRepository.saveAll(itemInfos);
    deckInfoRepository.saveAll(deckInfos);
    playerInfoRepository.saveAll(playerInfos);
    regionInfoRepository.saveAll(regionInfos);
  }

  public long countGameInfos() {
    return gameInfoRepository.count();
  }
}
