package dev.luzifer.algo.evaluation;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class StatisticEvaluation {

    protected static final double GEWINNER_GEWICHTUNG = 2d;
    protected static final double VERLIERER_GEWICHTUNG = 0.5d;

    protected final Mapper<MatchId, GameInfo> mapper;

}
