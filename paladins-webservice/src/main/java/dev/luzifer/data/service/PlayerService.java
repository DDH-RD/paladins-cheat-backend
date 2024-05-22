package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.entity.Deck;
import dev.luzifer.data.entity.ItemDraft;
import dev.luzifer.data.entity.Player;
import dev.luzifer.data.entity.Region;
import dev.luzifer.data.repository.DeckRepository;
import dev.luzifer.data.repository.ItemDraftRepository;
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

  // Make this an own service
  private final DeckRepository deckRepository;
  private final ItemDraftRepository itemDraftRepository;

  @Autowired
  public PlayerService(
      PlayerRepository playerRepository,
      RegionRepository regionRepository,
      DeckRepository deckRepository,
      ItemDraftRepository itemDraftRepository,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.playerRepository = playerRepository;
    this.regionRepository = regionRepository;
    this.deckRepository = deckRepository;
    this.itemDraftRepository = itemDraftRepository;
  }

  @Transactional
  public void savePlayers(ChampDto[] champDtoArray) {
    Set<Player> playersToSave = new HashSet<>();
    Arrays.stream(champDtoArray)
        .forEach(champ -> playersToSave.add(entityConverter.convertToPlayer(champ)));
    playerRepository.saveAll(playersToSave);
  }

  @Transactional
  public void saveDecks(ChampDto[] champDtoArray) {
    Set<Deck> decksToSave = new HashSet<>();
    Arrays.stream(champDtoArray)
        .forEach(champ -> decksToSave.add(entityConverter.convertToDeck(champ)));
    deckRepository.saveAll(decksToSave);
  }

  @Transactional
  public void saveItemDrafts(ChampDto[] champDtoArray) {
    Set<ItemDraft> itemDraftsToSave = new HashSet<>();
    Arrays.stream(champDtoArray)
        .forEach(champ -> itemDraftsToSave.add(entityConverter.convertToItemDraft(champ)));
    itemDraftRepository.saveAll(itemDraftsToSave);
  }

  @Transactional
  public void saveRegions(ChampDto[] champDtoArray) {
    Set<Region> regionsToSave = new HashSet<>();
    Arrays.stream(champDtoArray)
        .forEach(champ -> regionsToSave.add(entityConverter.convertToRegion(champ.getRegion())));
    regionRepository.saveAll(regionsToSave);
  }
}
