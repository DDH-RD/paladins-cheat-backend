package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BestBansEvaluation {

    private final GameDao gameDao;

    public EvaluationResult<List<Integer>> evaluate() {
        List<Integer> bans = fetchBans();
        Map<Integer, Integer> banCount = countBans(bans);

        return new EvaluationResult<>(banCount.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    public EvaluationResult<List<Integer>> evaluateForMap(String map) {
        List<Integer> bans = fetchBansForMap(map);
        Map<Integer, Integer> banCount = countBans(bans);

        return new EvaluationResult<>(banCount.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    private Map<Integer, Integer> countBans(List<Integer> bans) {
        return bans.stream()
                .collect(Collectors.toMap(ban -> ban, ban -> 1, Integer::sum));
    }

    private List<Integer> fetchBans() {
        return gameDao.getBans();
    }

    private List<Integer> fetchBansForMap(String map) {
        int mapId = gameDao.convertMapNameToId(map);
        return gameDao.getBansForMap(mapId);
    }
}
