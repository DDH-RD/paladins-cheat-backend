package dev.luzifer.data.evaluation;

import dev.luzifer.util.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated Poor performance
 */
@RequiredArgsConstructor
@Deprecated
public class BestCounterChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final double season;

    public Map<Integer, Integer> evaluate() {
        return evaluate(-1);
    }

    public Map<Integer, Integer> evaluate(int champCategory) {

        Set<ChampData> champDataSet = preparation(champCategory);
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

    private Set<ChampData> preparation(int champCategory) {

        List<ChampData> champDataList = gameDao.fetchChampDataForChamp(season, champId);
        Set<ChampData> champDataListExtended = new HashSet<>();

        for(ChampData champData : champDataList) {
            List<ChampData> champDataInMatch;
            if(champCategory == -1)
                champDataInMatch = gameDao.fetchChampDataForMatch(champData.getMatchId());
            else
                champDataInMatch = gameDao.fetchChampDataForMatchOfCategory(champData.getMatchId(), champCategory);
            champDataInMatch.removeIf(cd -> champData.getWon() == cd.getWon());
            champDataListExtended.addAll(champDataInMatch);
        }

        return new HashSet<>(champDataListExtended);
    }
}
