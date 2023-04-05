package dev.luzifer.data.evaluation;

import dev.luzifer.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.GameDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BestBanForMapEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final String mapName;
    private final GameDao gameDao;

    public Map<Integer, Integer> evaluate() {
        return MapUtil.sortByValue(preparation());
    }

    @Override
    public Map<Integer, Integer> evaluate(int champCategory) {
        throw new UnsupportedOperationException("This evaluation does not support a category");
    }

    private Map<Integer, Integer> preparation() {

        GameDto[] games = gameDao.fetchMatches();

        List<Integer[]> bannedChampsForMap = new ArrayList<>();
        for (GameDto game : games) {
            if (game.getMapName().equals(mapName)) {
                int ban1 = game.getBannedChamp1();
                int ban2 = game.getBannedChamp2();
                int ban3 = game.getBannedChamp3();
                int ban4 = game.getBannedChamp4();
                int ban5 = game.getBannedChamp5();
                int ban6 = game.getBannedChamp6();

                Integer[] banned = new Integer[] {ban1, ban2, ban3, ban4, ban5, ban6};
                bannedChampsForMap.add(banned);
            }
        }

        Map<Integer, Integer> bannedChamps = new HashMap<>();
        for (Integer[] banned : bannedChampsForMap) {
            for (Integer ban : banned) {
                if (bannedChamps.containsKey(ban)) {
                    bannedChamps.put(ban, bannedChamps.get(ban) + 1);
                } else {
                    bannedChamps.put(ban, 1);
                }
            }
        }

        return bannedChamps;
    }
}
