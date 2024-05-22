package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.data.entity.BannedChamp;
import dev.luzifer.data.entity.Map;
import dev.luzifer.data.entity.Match;
import dev.luzifer.data.repository.BannedChampRepository;
import dev.luzifer.data.repository.MapRepository;
import dev.luzifer.data.repository.MatchRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService extends BaseService {

  private final MatchRepository matchRepository;
  private final MapRepository mapRepository;
  private final BannedChampRepository bannedChampRepository;

  @Autowired
  public MatchService(
      MatchRepository matchRepository,
      MapRepository mapRepository,
      BannedChampRepository bannedChampRepository,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.matchRepository = matchRepository;
    this.mapRepository = mapRepository;
    this.bannedChampRepository = bannedChampRepository;
  }

  @Transactional
  public void processMatchData(GameDto[] gameDtoArray) {
    saveMatches(gameDtoArray);
    saveMaps(gameDtoArray);
    saveBannedChamps(gameDtoArray);
  }

  private void saveMatches(GameDto[] gameDtoArray) {
    Set<Match> matchesToSave = new HashSet<>();
    Arrays.stream(gameDtoArray)
        .forEach(game -> matchesToSave.add(entityConverter.convertToMatch(game)));
    matchRepository.saveAll(matchesToSave);
  }

  private void saveMaps(GameDto[] gameDtoArray) {
    Set<Map> mapsToSave = new HashSet<>();
    Arrays.stream(gameDtoArray)
        .forEach(game -> mapsToSave.add(entityConverter.convertToMap(game.getMapName())));
    mapRepository.saveAll(mapsToSave);
  }

  private void saveBannedChamps(GameDto[] gameDtoArray) {
    Set<BannedChamp> bannedChampsToSave = new HashSet<>();
    Arrays.stream(gameDtoArray)
        .forEach(game -> bannedChampsToSave.addAll(entityConverter.convertBannedChamps(game)));
    bannedChampRepository.saveAll(bannedChampsToSave);
  }

  @Cacheable("matchCount")
  public long countMatches() {
    return matchRepository.count();
  }
}
