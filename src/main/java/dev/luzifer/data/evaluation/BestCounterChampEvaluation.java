package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BestCounterChampEvaluation implements Evaluation<Integer> {

    private final int champId;
    private final GameDao gameDao;

    public Integer evaluate() {
        return evaluate(-1);
    }

    public Integer evaluate(int champCategory) {

       // see what champs are played against the given champ
        Map<GameDto, ChampDto[]> gamesWithChamps = preparation(champCategory);

        // count the points for each champ but only the enemy team
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

        // find the champ with the most points
        int bestChampId = -1;
        int bestChampPoints = -1;
        for (Map.Entry<Integer, Integer> entry : champPoints.entrySet()) {
            if (entry.getValue() > bestChampPoints) {
                bestChampId = entry.getKey();
                bestChampPoints = entry.getValue();
            }
        }

        return bestChampId;
    }

    private Map<GameDto, ChampDto[]> preparation(int champCategory) {

        GameDto[] games = gameDao.fetchMatches();
        List<GameDto> gamesWithChamp = new ArrayList<>();
        for (GameDto game : games) {
            for(ChampDto champ : game.getChamps()) {
                if(champ.getId() == champId) {
                    gamesWithChamp.add(game);
                    break;
                }
            }
        }

        Map<GameDto, ChampDto[]> gamesWithChamps = new HashMap<>();
        for (GameDto game : gamesWithChamp) {
            List<ChampDto> champs = new ArrayList<>();
            for (ChampDto champ : game.getChamps()) {
                if (champ.getId() != champId && (champCategory == -1 || champ.getCategoryId() == champCategory)) {
                    for(ChampDto enemyChamp : game.getChamps()) {
                        if(enemyChamp.getId() == champId) {
                            if(enemyChamp.getWon() != champ.getWon())
                                champs.add(champ);
                            break;
                        }
                    }
                }
            }
            gamesWithChamps.put(game, champs.toArray(new ChampDto[0]));
        }

        return gamesWithChamps;
    }
}
