package dev.luzifer.data.service;

import dev.luzifer.data.converter.EntityConverter;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.data.entity.Map;
import dev.luzifer.data.entity.Match;
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

  @Autowired
  public MatchService(
      MatchRepository matchRepository,
      MapRepository mapRepository,
      EntityConverter entityConverter) {
    super(entityConverter);
    this.matchRepository = matchRepository;
    this.mapRepository = mapRepository;
  }

  @Transactional
  public void processMatchData(GameDto[] gameDtoArray) {
    saveMatches(gameDtoArray);
    saveMaps(gameDtoArray);
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

  @Cacheable("matchCount")
  public long countMatches() {
    return matchRepository.count();
  }
}
