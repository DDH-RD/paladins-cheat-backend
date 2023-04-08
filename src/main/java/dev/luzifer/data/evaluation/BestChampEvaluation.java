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
public class BestChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final GameDao gameDao;
    private final GameController.MatchType matchType;

    @Override
    public Map<Integer, Integer> evaluate() {
        return evaluate(-1);
    }

    @Override
    public Map<Integer, Integer> evaluate(int champCategory) {

        Map<GameDto, ChampDto[]> gamesWithChamps = preparation(champCategory);

        Map<Integer, Integer> champPoints = new HashMap<>();
        for (Map.Entry<GameDto, ChampDto[]> entry : gamesWithChamps.entrySet()) {

            ChampDto[] champs = entry.getValue();
            GameDto game = entry.getKey();

            for (ChampDto champ : champs) {
                int points = champ.getWon() == 0 ?
                        Math.min(game.getTeam1Points(), game.getTeam2Points()) :
                        Math.max(game.getTeam1Points(), game.getTeam2Points());

                if (champPoints.containsKey(champ.getId()))
                    champPoints.put(champ.getId(), champPoints.get(champ.getId()) + points);
                else
                    champPoints.put(champ.getId(), points);
            }
        }

        return MapUtil.sortByValue(champPoints);
    }

    private Map<GameDto, ChampDto[]> preparation(int champCategory) {
        Map<GameDto, ChampDto[]> gamesWithChamps = new HashMap<>();
        for (GameDto game : gameDao.fetchMatches(matchType)) {
            List<ChampDto> champsForCategory = new ArrayList<>();
            for (ChampDto champ : game.getChamps()) {
                if (champCategory == -1 || champ.getCategoryId() == champCategory) {
                    champsForCategory.add(champ);
                }
            }
            gamesWithChamps.put(game, champsForCategory.toArray(new ChampDto[0]));
        }
        return gamesWithChamps;
    }
}
