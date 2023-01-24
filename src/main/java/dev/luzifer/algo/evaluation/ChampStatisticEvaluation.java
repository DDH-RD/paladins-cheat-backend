package dev.luzifer.algo.evaluation;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;

/*
 * Die besten Counter gegen Champ X. À wie oft hat Champ X gegen Champ Y gespielt?
 * Die besten Teamkonstellationen. À wie oft hat Champ X mit Champ Y gespielt?
 */
public class ChampStatisticEvaluation extends StatisticEvaluation {

    public ChampStatisticEvaluation(Mapper<MatchId, GameInfo> mapper) {
        super(mapper);
    }
}
