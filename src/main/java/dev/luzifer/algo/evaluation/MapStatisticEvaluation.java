package dev.luzifer.algo.evaluation;

import dev.luzifer.algo.ChampType;
import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.TeamInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStatisticEvaluation extends StatisticEvaluation {

    protected static final double GEWINNER_GEWICHTUNG = 2d;
    protected static final double VERLIERER_GEWICHTUNG = 0.5d;

    public MapStatisticEvaluation(Mapper<MatchId, GameInfo> mapper) {
        super(mapper);
    }

    public long[] evaluateForMap(String mapName, ChampType champType) {

        Map<Long, Double> counter = new HashMap<>();

        for(MatchId matchId : mapper.getMappings().keySet()) {

            GameInfo gameInfo = mapper.getMapping(matchId);
            if(!gameInfo.getMapName().equals(mapName))
                continue;

            sortInBothTeams(counter, gameInfo, champType);
        }

        return sortByValue(counter);
    }

    public long[] evaluateAll(ChampType champType) {

        Map<Long, Double> counter = new HashMap<>();

        for(MatchId matchId : mapper.getMappings().keySet()) {
            GameInfo gameInfo = mapper.getMapping(matchId);
            sortInBothTeams(counter, gameInfo, champType);
        }

        return sortByValue(counter);
    }

    private void sortInBothTeams(Map<Long, Double> counter, GameInfo gameInfo, ChampType champType) {

        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();

        long[] winnerIds = champType == ChampType.PLAYED ? winner.getPlayedChampIds() : winner.getBannedChampIds();
        long[] loserIds = champType == ChampType.PLAYED ? loser.getPlayedChampIds() : loser.getBannedChampIds();

        sortIn(counter, winnerIds, true);
        sortIn(counter, loserIds, false);
    }

    private long[] sortByValue(Map<Long, Double> counter) {

        List<Long> sortedList = new ArrayList<>(counter.keySet());
        sortedList.sort(Comparator.comparingDouble(counter::get));

        Collections.reverse(sortedList);

        return sortedList.stream().mapToLong(id -> id).toArray();
    }

    private void sortIn(Map<Long, Double> counter, long[] ids, boolean won) {

        double multiplicator = won ? GEWINNER_GEWICHTUNG : VERLIERER_GEWICHTUNG;

        int index = 0;
        while(index < ids.length) {

            long id = ids[index++];
            if(!counter.containsKey(id))
                counter.put(id, multiplicator);
            else
                counter.put(id, counter.get(id) + multiplicator);
        }
    }
}
