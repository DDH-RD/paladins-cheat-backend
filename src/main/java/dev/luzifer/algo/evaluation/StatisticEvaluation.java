package dev.luzifer.algo.evaluation;

import dev.luzifer.algo.ChampType;
import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.ChampInfo;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.TeamInfo;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class StatisticEvaluation {

    protected final Mapper<MatchId, GameInfo> mapper;
    
    protected long[] sortByValue(Map<Long, Double> counter) {
        
        List<Long> sortedList = new ArrayList<>(counter.keySet());
        sortedList.sort(Comparator.comparingDouble(counter::get));
        
        Collections.reverse(sortedList);
        
        return sortedList.stream().mapToLong(id -> id).toArray();
    }
    
    protected void sortInSingleTeamPlayed(Map<Long, Double> counter, TeamInfo teamInfo) {
        sortIn(counter, teamInfo, ChampType.PLAYED);
    }
    
    protected void sortInBothTeams(Map<Long, Double> counter, GameInfo gameInfo, ChampType champType) {
        
        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();

        sortIn(counter, winner, champType);
        sortIn(counter, loser, champType);
    }
    
    protected void sortIn(Map<Long, Double> counter, TeamInfo teamInfo, ChampType champType) {

        ChampInfo[] champInfos = champType == ChampType.PLAYED ? teamInfo.getPlayedChamps() : teamInfo.getBannedChamps();
        double gewichtung = 0.5 * teamInfo.getPoints();

        int index = 0;
        while(index < champInfos.length) {
            
            ChampInfo champInfo = champInfos[index++];
            if(!counter.containsKey(champInfo.getId()))
                counter.put(champInfo.getId(), gewichtung);
            else
                counter.put(champInfo.getId(), counter.get(champInfo.getId()) + gewichtung);
        }
    }

}
