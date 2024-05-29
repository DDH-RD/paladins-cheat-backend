package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.entity.PaladinsChamp;
import dev.luzifer.data.entity.PlayedChamp;
import dev.luzifer.data.repository.PaladinsChampRepository;
import dev.luzifer.data.repository.PlayedChampRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChampService extends BaseService {

  private final PlayedChampRepository playedChampRepository;
  private final PaladinsChampRepository paladinsChampRepository;

  @Autowired
  public ChampService(
      PlayedChampRepository playedChampRepository,
      PaladinsChampRepository paladinsChampRepository,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.playedChampRepository = playedChampRepository;
    this.paladinsChampRepository = paladinsChampRepository;
  }

  @Transactional
  public void processChamps(ChampDto[] champDtos) {
    Set<PlayedChamp> playedChampsToSave = new HashSet<>();
    Set<PaladinsChamp> paladinsChampsToSave = new HashSet<>();

    Arrays.stream(champDtos)
        .forEach(
            champ -> {
              playedChampsToSave.add(entityConverter.convertToPlayedChamp(champ));
              paladinsChampsToSave.add(entityConverter.convertToPaladinsChamp(champ));
            });

    playedChampRepository.saveAll(playedChampsToSave);
    paladinsChampRepository.saveAll(paladinsChampsToSave);
  }
}
