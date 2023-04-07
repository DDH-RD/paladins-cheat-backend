package dev.luzifer.data.evaluation;

import dev.luzifer.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import dev.luzifer.spring.controller.GameController;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BestCounterChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final GameController.MatchType matchType;

    public Map<Integer, Integer> evaluate() {
        return evaluate(-1);
    }

    public Map<Integer, Integer> evaluate(int champCategory) {

        Map<GameDto, ChampDto[]> gamesWithChamps = preparation(champCategory);

        Map<Integer, Integer> champPoints = new HashMap<>();
        for (Map.Entry<GameDto, ChampDto[]> entry : gamesWithChamps.entrySet()) {

            ChampDto[] champs = entry.getValue();
            GameDto game = entry.getKey();

            for (ChampDto champ : champs) {
                int points;
                if(champ.getWon() == 0)
                    points = Math.min(game.getTeam1Points(), game.getTeam2Points());
                else
                    points = Math.max(game.getTeam1Points(), game.getTeam2Points());

                if (champPoints.containsKey(champ.getId())) {
                    champPoints.put(champ.getId(), champPoints.get(champ.getId()) + points);
                } else {
                    champPoints.put(champ.getId(), points);
                }
            }
        }

        return MapUtil.sortByValue(champPoints);
    }

    private Map<GameDto, ChampDto[]> preparation(int champCategory) {

        Map<GameDto, ChampDto[]> resultMap = new HashMap<>();

        GameDto[] games = gameDao.fetchMatchesWithChamp(matchType, champId);
        for(GameDto game : games) {
            ChampDto[] champs = gameDao.fetchChampsForMatch(game.getId());
            List<ChampDto> enemyChamps = new ArrayList<>();
            for(ChampDto champ : champs) {
                if(champ.getId() == champId) {
                    for(ChampDto enemyChamp : champs) {
                        if(enemyChamp.getWon() != champ.getWon() && (champCategory == -1 || enemyChamp.getCategoryId() == champCategory)) {
                            enemyChamps.add(enemyChamp);
                        }
                    }
                    break;
                }
            }
            resultMap.put(game, enemyChamps.toArray(new ChampDto[0]));
        }

        return resultMap;
    }
}
