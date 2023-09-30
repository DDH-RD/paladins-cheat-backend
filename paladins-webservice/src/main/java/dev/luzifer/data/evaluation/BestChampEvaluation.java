package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BestChampEvaluation {

    private final GameDao gameDao;

    public BestChampEvaluation(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public EvaluationResult<List<Integer>> evaluate() {
        Map<Integer, List<Integer>> champsMap = gameDao.getChamps();
        Map<Integer, Integer> weightMap = weightChamps(champsMap);

        return new EvaluationResult<>(weightMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    public EvaluationResult<List<Integer>> evaluateForMap(String map) {
        Map<Integer, List<Integer>> champsMap = gameDao.getChampsForMap(gameDao.convertMapNameToId(map));
        Map<Integer, Integer> weightMap = weightChamps(champsMap);

        return new EvaluationResult<>(weightMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    public EvaluationResult<List<Integer>> evaluateForCategory(int categoryId) {
        Map<Integer, List<Integer>> champsMap = gameDao.getChampsForCategory(categoryId);
        Map<Integer, Integer> weightMap = weightChamps(champsMap);

        return new EvaluationResult<>(weightMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    public EvaluationResult<List<Integer>> evaluateForCategoryOnMap(int categoryId, String map) {
        Map<Integer, List<Integer>> champsMap = gameDao.getChampsForCategoryOnMap(
                categoryId,
                gameDao.convertMapNameToId(map));
        Map<Integer, Integer> weightMap = weightChamps(champsMap);

        return new EvaluationResult<>(weightMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(8)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    private Map<Integer, Integer> weightChamps(Map<Integer, List<Integer>> champMap) {
        Map<Integer, Integer> weightMap = new HashMap<>();
        for(Map.Entry<Integer, List<Integer>> entry : champMap.entrySet()) {
            for(Integer champId : entry.getValue()) {
                if(!weightMap.containsKey(champId)) {
                    weightMap.put(champId, 0);
                }

                weightMap.put(champId, weightMap.get(champId) + entry.getKey());
            }
        }

        return weightMap;
    }

}
