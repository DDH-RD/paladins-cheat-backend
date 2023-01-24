package dev.luzifer;

import dev.luzifer.algo.evaluation.ChampStatisticEvaluation;
import dev.luzifer.algo.evaluation.MapStatisticEvaluation;
import dev.luzifer.algo.ChampType;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.MatchMapper;
import dev.luzifer.data.match.info.TeamInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluationTests {

    @Test
    public void champEvaluationCounterTest() {
        
        MatchMapper matchMapper = new MatchMapper();
        
        long[] played = {1L, 1L, 3L, 2L, 3L, 1L};
        long[] banned = {4L, 5L, 6L, 6L, 6L, 5L};
        
        TeamInfo winner = new TeamInfo(played, banned);
        TeamInfo loser = new TeamInfo(banned, banned);
        
        matchMapper.map(MatchId.of(1), new GameInfo("xD", winner, loser));
        matchMapper.map(MatchId.of(2), new GameInfo("xDD", loser, winner));
        
        long expected = 6L;
        long actual = new ChampStatisticEvaluation(matchMapper).evaluateCounterFor(2L);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void mapEvaluationGewichtungTest() {

    }

    @Test
    public void mapEvaluationForMapTest() {

        MatchMapper matchMapper = new MatchMapper();

        long[] played = {1L, 1L, 3L, 2L, 3L, 1L};
        long[] banned = {4L, 5L, 6L, 6L, 6L, 5L};

        TeamInfo winner = new TeamInfo(played, banned);
        TeamInfo loser = new TeamInfo(played, banned);

        matchMapper.map(MatchId.of(1), new GameInfo("ballo", winner, loser));
        matchMapper.map(MatchId.of(2), new GameInfo("x", winner, loser));
        matchMapper.map(MatchId.of(3), new GameInfo("D", winner, loser));

        long[] expectedPlayed = {1L, 3L, 2L};
        long[] expectedBanned = {6L, 5L, 4L};

        MapStatisticEvaluation mapStatisticEvaluation = new MapStatisticEvaluation(matchMapper);
        long[] actualPlayed = mapStatisticEvaluation.evaluateForMap("ballo", ChampType.PLAYED);
        long[] actualBanned = mapStatisticEvaluation.evaluateForMap("x", ChampType.BANNED);

        assertArrayEquals(expectedPlayed, actualPlayed);
        assertArrayEquals(expectedBanned, actualBanned);
    }

    @Test
    public void mapEvaluationAllTest() {

        MatchMapper matchMapper = new MatchMapper();

        long[] played = {1L, 1L, 3L, 2L, 3L, 1L};
        long[] banned = {4L, 5L, 6L, 6L, 6L, 5L};

        TeamInfo winner = new TeamInfo(played, banned);
        TeamInfo loser = new TeamInfo(played, banned);

        matchMapper.map(MatchId.of(1), new GameInfo("ballo", winner, loser));
        matchMapper.map(MatchId.of(2), new GameInfo("x", winner, loser));
        matchMapper.map(MatchId.of(3), new GameInfo("D", winner, loser));

        long[] expectedPlayed = {1L, 3L, 2L};
        long[] expectedBanned = {6L, 5L, 4L};

        MapStatisticEvaluation mapStatisticEvaluation = new MapStatisticEvaluation(matchMapper);
        long[] actualPlayed = mapStatisticEvaluation.evaluateAll(ChampType.PLAYED);
        long[] actualBanned = mapStatisticEvaluation.evaluateAll(ChampType.BANNED);

        assertArrayEquals(expectedPlayed, actualPlayed);
        assertArrayEquals(expectedBanned, actualBanned);
    }

}
