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
public class BestTalentForChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final Double season;

    @Override
    public Map<Integer, Integer> evaluate() {

        List<ChampData> champs = preparation();
        Map<Integer, Integer> map = new HashMap<>();

        for(ChampData champData : champs) {

            int talentId = champData.getTalentId();
            int pointsToAdd = champData.getWon() == 0 ?
                    Math.min(champData.getTeam1Points(), champData.getTeam2Points()) :
                    Math.max(champData.getTeam1Points(), champData.getTeam2Points());

            if(map.containsKey(talentId))
                map.put(talentId, map.get(talentId) + pointsToAdd);
            else
                map.put(talentId, pointsToAdd);
        }

        return MapUtil.sortByValue(map);
    }

    @Override
    public Map<Integer, Integer> evaluate(int champCategory) {
        throw new UnsupportedOperationException("There is no category for this evaluation");
    }

    private List<ChampData> preparation() {
        return gameDao.fetchChampDataForChamp(season, champId);
    }
}
