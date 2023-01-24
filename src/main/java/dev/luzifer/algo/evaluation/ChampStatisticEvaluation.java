package dev.luzifer.algo.evaluation;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.TeamInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            Optional<TeamInfo> oppositeTeam = getOppositeTeam(id, gameInfo);
            
            if(!oppositeTeam.isPresent()) // not in match
                continue;
            
            sortInSingleTeam(counter, gameInfo, oppositeTeam.get());
        }
        
        long[] counterAsArray = sortByValue(counter);
        return counterAsArray.length > 0 ? counterAsArray[0] : -1;
    }
    
    private Optional<TeamInfo> getOppositeTeam(long id, GameInfo gameInfo) {
        
        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();
        
        if(arrayContains(winner.getPlayedChampIds(), id))
            return Optional.of(loser);
        else if(arrayContains(loser.getPlayedChampIds(), id))
            return Optional.of(winner);
        
        return Optional.empty();
    }
    
    private boolean arrayContains(long[] array, long id) {
        for(long l : array) {
            if(l == id)
                return true;
        }
        return false;
    }
}
