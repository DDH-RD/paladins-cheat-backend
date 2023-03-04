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
public class BestChampForMapEvaluation {

    private final String mapName;
    private final GameDao gameDao;

    public Map<ChampDto, Integer> evaluate() {
        return evaluate(-1);
    }

    public Map<ChampDto, Integer> evaluate(int champCategory) {

        Map<GameDto, ChampDto[]> gamesWithChamps = preparation(champCategory);

        Map<ChampDto, Integer> champPoints = new HashMap<>();
        for (Map.Entry<GameDto, ChampDto[]> entry : gamesWithChamps.entrySet()) {

            ChampDto[] champs = entry.getValue();
            GameDto game = entry.getKey();

            for (ChampDto champ : champs) {
                int points;
                if(champ.getWon() == 0)
                    points = Math.min(game.getTeam1Points(), game.getTeam2Points());
                else
                    points = Math.max(game.getTeam1Points(), game.getTeam2Points());

                if (champPoints.containsKey(champ)) {
                    champPoints.put(champ, champPoints.get(champ) + points);
                } else {
                    champPoints.put(champ, points);
                }
            }
        }

        return champPoints;
    }

    private Map<GameDto, ChampDto[]> preparation(int champCategory) {

        GameDto[] games = gameDao.fetchMatches();

        List<GameDto> gamesForMap = new ArrayList<>();
        for (GameDto game : games) {
            if (game.getMapName().equals(mapName)) {
                gamesForMap.add(game);
            }
        }

        Map<GameDto, ChampDto[]> gamesWithChamps = new HashMap<>();
        for (GameDto game : gamesForMap) {
            ChampDto[] champs = gameDao.fetchChampsForMatch(game.getId());
            List<ChampDto> champsForCategory = new ArrayList<>();
            for (ChampDto champ : champs) {
                if (champCategory == -1 || champ.getCategoryId() == champCategory) { // -1 = all champs
                    champsForCategory.add(champ);
                }
            }
            gamesWithChamps.put(game, champsForCategory.toArray(new ChampDto[0]));
        }

        return gamesWithChamps;
    }

}
