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
}
