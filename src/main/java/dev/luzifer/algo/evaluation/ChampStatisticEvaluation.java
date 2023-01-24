package dev.luzifer.algo.evaluation;

import dev.luzifer.algo.ChampType;
import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.TeamInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated in Arbeit
 */
@Deprecated
public class ChampStatisticEvaluation extends StatisticEvaluation {

    public ChampStatisticEvaluation(Mapper<MatchId, GameInfo> mapper) {
        super(mapper);
    }

    /*
     * Possibility, dass es immer ein Tank wird.
     * Category mit angeben?
     */
    public long evaluateCounterFor(long id) {
        
        Map<Long, Double> counter = new HashMap<>();
        
        for(MatchId matchId : mapper.getMappings().keySet()) {
            
            GameInfo gameInfo = mapper.getMapping(matchId);
            if(!matchContainsChamp(id))
                continue;
            
            sortInBothTeams(counter, gameInfo, ChampType.PLAYED);
        }
        
        long[] counterAsArray = sortByValue(counter);
        return counterAsArray.length > 0 ? counterAsArray[0] : -1;
    }
    
    private boolean matchContainsChamp(long id) {
        return mapper.getMappings().values().stream().anyMatch(gameInfo -> hasAnyTeamChamp(id, gameInfo));
    }
    
    private boolean hasAnyTeamChamp(long id, GameInfo gameInfo) {
        
        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();
        
        return arrayContains(winner.getPlayedChampIds(), id)
                || arrayContains(loser.getPlayedChampIds(), id);
    }
    
    private boolean arrayContains(long[] array, long id) {
        for(long l : array) {
            if(l == id)
                return true;
        }
        return false;
    }
}
