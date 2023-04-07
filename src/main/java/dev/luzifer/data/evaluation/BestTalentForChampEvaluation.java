package dev.luzifer.data.evaluation;

import dev.luzifer.MapUtil;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import dev.luzifer.spring.controller.GameController;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BestTalentForChampEvaluation implements Evaluation<Map<Integer, Integer>> {

    private final int champId;
    private final GameDao gameDao;
    private final GameController.MatchType matchType;

    @Override
    public Map<Integer, Integer> evaluate() {

        Map<GameDto, ChampDto> champs = preparation();
        Map<Integer, Integer> map = new HashMap<>();

        for(Map.Entry<GameDto, ChampDto> entry : champs.entrySet()) {

            GameDto game = entry.getKey();
            ChampDto champDto = entry.getValue();

            int talentId = champDto.getTalentId();
            int pointsToAdd = champDto.getWon() == 0 ?
                    Math.min(game.getTeam1Points(), game.getTeam2Points()) :
                    Math.max(game.getTeam1Points(), game.getTeam2Points());

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

    private Map<GameDto, ChampDto> preparation() {

        Map<GameDto, ChampDto> champs = new HashMap<>();

        GameDto[] games = gameDao.fetchMatchesWithChamp(matchType, champId);
        for(GameDto game : games) {
            for(ChampDto champ : game.getChamps()) {
                if(champ.getId() == champId) {
                    champs.put(game, champ);
                    break;
                }
            }
        }

        return champs;
    }
}
