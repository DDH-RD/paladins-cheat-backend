package dev.luzifer;

import dev.luzifer.algo.MatchStatisticEvaluation;
import dev.luzifer.algo.ResultType;
import dev.luzifer.data.gamestats.GameInfo;
import dev.luzifer.data.gamestats.MatchId;
import dev.luzifer.data.gamestats.MatchMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluationTests {

    @Test
    public void evaluationTest() {

        MatchMapper matchMapper = new MatchMapper();

        long[] played = {1L, 1L, 3L, 2L, 3L, 1L};
        long[] banned = {4L, 5L, 6L, 6L, 6L, 5L};

        matchMapper.map(MatchId.of(1), new GameInfo("ballo", played, banned));
        matchMapper.map(MatchId.of(2), new GameInfo("x", played, banned));
        matchMapper.map(MatchId.of(3), new GameInfo("D", played, banned));

        long[] expectedPlayed = {1L, 3L, 2L};
        long[] expectedBanned = {6L, 5L, 4L};

        MatchStatisticEvaluation matchStatisticEvaluation = new MatchStatisticEvaluation(matchMapper);
        long[] actualPlayed = matchStatisticEvaluation.evaluate(ResultType.PLAYED);
        long[] actualBanned = matchStatisticEvaluation.evaluate(ResultType.BANNED);

        assertArrayEquals(expectedPlayed, actualPlayed);
        assertArrayEquals(expectedBanned, actualBanned);
    }

}
