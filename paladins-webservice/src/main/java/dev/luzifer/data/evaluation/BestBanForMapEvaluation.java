package dev.luzifer.data.evaluation;

import dev.luzifer.util.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampData;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class BestBanForMapEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final String mapName;
    private final GameDao gameDao;
    private final Double season;

    public Map<Integer, Integer> evaluate() {
        return MapUtil.sortByValue(preparation());
    }

    @Override
    public Map<Integer, Integer> evaluate(int champCategory) {
        throw new UnsupportedOperationException("This evaluation does not support a category");
    }

    private Map<Integer, Integer> preparation() {

        Map<Integer, Integer[]> bannedChampsForMatch = new HashMap<>();
        Set<ChampData> champDataList = gameDao.fetchChampDataForMap(season, mapName);

        for(ChampData champData : champDataList) {
            int matchId = champData.getMatchId();
            bannedChampsForMatch.computeIfAbsent(matchId, k -> new Integer[] {
                    champData.getBannedChamp1(),
                    champData.getBannedChamp2(),
                    champData.getBannedChamp3(),
                    champData.getBannedChamp4(),
                    champData.getBannedChamp5(),
                    champData.getBannedChamp6()});
        }

        Map<Integer, Integer> bannedChamps = new HashMap<>();
        for (Integer[] banned : bannedChampsForMatch.values()) {
            for (Integer ban : banned) {
                if (bannedChamps.containsKey(ban))
                    bannedChamps.put(ban, bannedChamps.get(ban) + 1);
                else
                    bannedChamps.put(ban, 1);
            }
        }

        return bannedChamps;
    }
}
