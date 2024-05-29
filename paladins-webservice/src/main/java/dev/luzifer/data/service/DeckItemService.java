package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.entity.Deck;
import dev.luzifer.data.entity.ItemDraft;
import dev.luzifer.data.repository.DeckRepository;
import dev.luzifer.data.repository.ItemDraftRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeckItemService extends BaseService {

  private final DeckRepository deckRepository;
  private final ItemDraftRepository itemDraftRepository;

  @Autowired
  public DeckItemService(
      DeckRepository deckRepository,
      ItemDraftRepository itemDraftRepository,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.deckRepository = deckRepository;
    this.itemDraftRepository = itemDraftRepository;
  }

  @Transactional
  public void processDeckItems(ChampDto[] champDtos) {
    Set<Deck> decksToSave = new HashSet<>();
    Set<ItemDraft> itemDraftsToSave = new HashSet<>();

    Arrays.stream(champDtos)
        .forEach(
            champ -> {
              decksToSave.add(entityConverter.convertToDeck(champ));
              itemDraftsToSave.add(entityConverter.convertToItemDraft(champ));
            });

    deckRepository.saveAll(decksToSave);
    itemDraftRepository.saveAll(itemDraftsToSave);
  }
}
