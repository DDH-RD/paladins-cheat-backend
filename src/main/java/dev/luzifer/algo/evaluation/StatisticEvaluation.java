package dev.luzifer.algo.evaluation;

import dev.luzifer.algo.ChampType;
import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.TeamInfo;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class StatisticEvaluation {

    protected static final double GEWINNER_GEWICHTUNG = 2d;
    protected static final double VERLIERER_GEWICHTUNG = 0.5d;

    protected final Mapper<MatchId, GameInfo> mapper;
    
    protected long[] sortByValue(Map<Long, Double> counter) {
        
        List<Long> sortedList = new ArrayList<>(counter.keySet());
        sortedList.sort(Comparator.comparingDouble(counter::get));
        
        Collections.reverse(sortedList);
        
        return sortedList.stream().mapToLong(id -> id).toArray();
    }
    
    protected void sortInBothTeams(Map<Long, Double> counter, GameInfo gameInfo, ChampType champType) {
        
        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();
        
        long[] winnerIds = champType == ChampType.PLAYED ? winner.getPlayedChampIds() : winner.getBannedChampIds();
        long[] loserIds = champType == ChampType.PLAYED ? loser.getPlayedChampIds() : loser.getBannedChampIds();
        
        sortIn(counter, winnerIds, true);
        sortIn(counter, loserIds, false);
    }
    
    protected void sortIn(Map<Long, Double> counter, long[] ids, boolean won) {
        
        double gewichtung = won ? GEWINNER_GEWICHTUNG : VERLIERER_GEWICHTUNG;
        
        int index = 0;
        while(index < ids.length) {
            
            long id = ids[index++];
            if(!counter.containsKey(id))
                counter.put(id, gewichtung);
            else
                counter.put(id, counter.get(id) + gewichtung);
        }
    }

}
