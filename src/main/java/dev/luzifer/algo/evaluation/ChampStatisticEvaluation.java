package dev.luzifer.algo.evaluation;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.ChampInfo;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.info.TeamInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChampStatisticEvaluation extends StatisticEvaluation {

    public ChampStatisticEvaluation(Mapper<MatchId, GameInfo> mapper) {
        super(mapper);
    }

    /*
     * Possibility, dass es immer ein Tank wird.
     * Category mit angeben?
     */
    public long[] evaluateCounterFor(long id) {
        
        Map<Long, Double> counter = new HashMap<>();
        
        for(MatchId matchId : mapper.getMappings().keySet()) {
            
            GameInfo gameInfo = mapper.getMapping(matchId);
            Optional<TeamInfo> oppositeTeam = getOppositeTeam(id, gameInfo);
            
            if(!oppositeTeam.isPresent()) // not in match
                continue;
            
            sortInSingleTeamPlayed(counter, oppositeTeam.get());
        }
        
        long[] counterAsArray = sortByValue(counter);
        return counterAsArray.length > 0 ? counterAsArray : new long[]{-1};
    }
    
    public long[] evaluateWingmanFor(long id) {
        
        Map<Long, Double> counter = new HashMap<>();
        
        for(MatchId matchId : mapper.getMappings().keySet()) {
            
            GameInfo gameInfo = mapper.getMapping(matchId);
            Optional<TeamInfo> oppositeTeam = getOppositeTeam(id, gameInfo);
            
            if(!oppositeTeam.isPresent()) // not in match
                continue;
            
            TeamInfo team = oppositeTeam.get() == gameInfo.getWinnerTeam() ?
                    gameInfo.getLoserTeam() : gameInfo.getWinnerTeam();
            
            sortInSingleTeamPlayed(counter, team);
        }
        
        counter.remove(id); // so it will not be its own wingman
        
        long[] counterAsArray = sortByValue(counter);
        return counterAsArray.length > 0 ? counterAsArray : new long[]{-1};
    }
    
    private Optional<TeamInfo> getOppositeTeam(long id, GameInfo gameInfo) {
        
        TeamInfo winner = gameInfo.getWinnerTeam();
        TeamInfo loser = gameInfo.getLoserTeam();
        
        if(arrayContains(winner.getPlayedChamps(), id))
            return Optional.of(loser);
        else if(arrayContains(loser.getPlayedChamps(), id))
            return Optional.of(winner);
        
        return Optional.empty();
    }
    
    private boolean arrayContains(ChampInfo[] array, long id) {
        for(ChampInfo l : array) {
            if(l.getId() == id)
                return true;
        }
        return false;
    }
}
