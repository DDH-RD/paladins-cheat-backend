package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.entity.Player;
import dev.luzifer.data.entity.Region;
import dev.luzifer.data.repository.PlayerRepository;
import dev.luzifer.data.repository.RegionRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService extends BaseService {

  private final PlayerRepository playerRepository;
  private final RegionRepository regionRepository;

  private final DeckItemService deckItemService;

  @Autowired
  public PlayerService(
      PlayerRepository playerRepository,
      RegionRepository regionRepository,
      DeckItemService deckItemService,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.playerRepository = playerRepository;
    this.regionRepository = regionRepository;
    this.deckItemService = deckItemService;
  }

  @Transactional
  public void processPlayers(ChampDto[] champDtos) {
    Set<Player> playersToSave = new HashSet<>();
    Set<Region> regionsToSave = new HashSet<>();

    Arrays.stream(champDtos)
        .forEach(
            champ -> {
              playersToSave.add(entityConverter.convertToPlayer(champ));
              regionsToSave.add(entityConverter.convertToRegion(champ.getRegion()));
            });

    playerRepository.saveAll(playersToSave);
    regionRepository.saveAll(regionsToSave);
    deckItemService.processDeckItems(champDtos);
  }
}
