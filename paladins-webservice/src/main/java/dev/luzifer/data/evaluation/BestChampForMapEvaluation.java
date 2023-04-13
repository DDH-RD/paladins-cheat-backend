package dev.luzifer.data.evaluation;

import dev.luzifer.util.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BestChampForMapEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final String mapName;
    private final GameDao gameDao;
    private final GameController.MatchType matchType;

    public Map<Integer, Integer> evaluate() {
        return evaluate(-1);
    }

    public Map<Integer, Integer> evaluate(int champCategory) {

        List<ChampData> champDataSet = preparation(champCategory);
        Map<Integer, Integer> champPoints = new HashMap<>();

        for (ChampData champData : champDataSet) {
            int points = champData.getWon() == 0 ?
                    Math.min(champData.getTeam1Points(), champData.getTeam2Points()) :
                    Math.max(champData.getTeam1Points(), champData.getTeam2Points());

            if (champPoints.containsKey(champData.getChampId()))
                champPoints.put(champData.getChampId(), champPoints.get(champData.getChampId()) + points);
            else
                champPoints.put(champData.getChampId(), points);
        }

        return MapUtil.sortByValue(champPoints);
    }

    private List<ChampData> preparation(int champCategory) {
        if(champCategory == -1)
            return gameDao.fetchChampDataForMap(matchType, mapName);
        else
            return gameDao.fetchChampDataForMapOfCategory(matchType, mapName, champCategory);
    }

}
