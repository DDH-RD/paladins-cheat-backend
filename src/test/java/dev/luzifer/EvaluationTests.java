package dev.luzifer;

import dev.luzifer.algo.evaluation.ChampStatisticEvaluation;
import dev.luzifer.algo.evaluation.MapStatisticEvaluation;
import dev.luzifer.algo.ChampType;
import dev.luzifer.data.match.info.ChampInfo;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.MatchMapper;
import dev.luzifer.data.match.info.TeamInfo;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;

public class EvaluationTests {

    @Test
    public void champEvaluationWingmanTest() {
        
        MatchMapper matchMapper = new MatchMapper();

        ChampInfo champInfo = new ChampInfo(1, 1, 1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo2 = new ChampInfo(2, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo3 = new ChampInfo(3, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo4 = new ChampInfo(4, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);

        TeamInfo winner = new TeamInfo(2, new ChampInfo[] {champInfo, champInfo, champInfo2}, null);
        TeamInfo loser = new TeamInfo(1, new ChampInfo[] {champInfo3, champInfo3, champInfo4}, null);
        
        matchMapper.map(MatchId.of(1), new GameInfo("xD", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        
        long[] expected = {1L};
        long[] actual = new ChampStatisticEvaluation(matchMapper).evaluateWingmanFor(2L);

        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void champEvaluationCounterTest() {
        
        MatchMapper matchMapper = new MatchMapper();

        ChampInfo champInfo = new ChampInfo(1, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo2 = new ChampInfo(2, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo3 = new ChampInfo(3, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo4 = new ChampInfo(4, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);

        TeamInfo winner = new TeamInfo(2, new ChampInfo[] {champInfo, champInfo, champInfo2}, null);
        TeamInfo loser = new TeamInfo(1, new ChampInfo[] {champInfo3, champInfo3, champInfo4}, null);
        
        matchMapper.map(MatchId.of(1), new GameInfo("xD", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        matchMapper.map(MatchId.of(2), new GameInfo("xDD", 2, loser, winner, 12, Instant.now().toEpochMilli()));
        
        long[] expected = {3L, 4L};
        long[] actual = new ChampStatisticEvaluation(matchMapper).evaluateCounterFor(1L);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void mapEvaluationGewichtungTest() {

    }

    @Test
    public void mapEvaluationForMapTest() {

        MatchMapper matchMapper = new MatchMapper();

        ChampInfo champInfo = new ChampInfo(1, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo2 = new ChampInfo(2, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo3 = new ChampInfo(3, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo4 = new ChampInfo(4, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);

        TeamInfo winner = new TeamInfo(3, new ChampInfo[] {champInfo, champInfo, champInfo2}, new ChampInfo[] {champInfo});
        TeamInfo loser = new TeamInfo(1, new ChampInfo[] {champInfo3, champInfo3, champInfo4}, new ChampInfo[] {champInfo});

        matchMapper.map(MatchId.of(1), new GameInfo("ballo", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        matchMapper.map(MatchId.of(2), new GameInfo("x", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        matchMapper.map(MatchId.of(3), new GameInfo("D", 2, winner, loser, 12, Instant.now().toEpochMilli()));

        long[] expectedPlayed = {1L, 2L, 3L, 4L};
        long[] expectedBanned = {1L};

        MapStatisticEvaluation mapStatisticEvaluation = new MapStatisticEvaluation(matchMapper);
        long[] actualPlayed = mapStatisticEvaluation.evaluateForMap("ballo", ChampType.PLAYED);
        long[] actualBanned = mapStatisticEvaluation.evaluateForMap("x", ChampType.BANNED);

        assertArrayEquals(expectedPlayed, actualPlayed);
        assertArrayEquals(expectedBanned, actualBanned);
    }

    @Test
    public void mapEvaluationAllTest() {

        MatchMapper matchMapper = new MatchMapper();

        ChampInfo champInfo = new ChampInfo(1, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo2 = new ChampInfo(2, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo3 = new ChampInfo(3, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        ChampInfo champInfo4 = new ChampInfo(4, 1,  1, null, null, 0, 0, 0, 0, 0, 0, 0, 0);

        TeamInfo winner = new TeamInfo(3, new ChampInfo[] {champInfo, champInfo, champInfo2}, new ChampInfo[] {champInfo});
        TeamInfo loser = new TeamInfo(1, new ChampInfo[] {champInfo3, champInfo3, champInfo4}, new ChampInfo[] {champInfo});

        matchMapper.map(MatchId.of(1), new GameInfo("ballo", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        matchMapper.map(MatchId.of(2), new GameInfo("x", 2, winner, loser, 12, Instant.now().toEpochMilli()));
        matchMapper.map(MatchId.of(3), new GameInfo("D", 2, winner, loser, 12, Instant.now().toEpochMilli()));

        long[] expectedPlayed = {1L, 2L, 3L, 4L};
        long[] expectedBanned = {1L};

        MapStatisticEvaluation mapStatisticEvaluation = new MapStatisticEvaluation(matchMapper);
        long[] actualPlayed = mapStatisticEvaluation.evaluateAll(ChampType.PLAYED);
        long[] actualBanned = mapStatisticEvaluation.evaluateAll(ChampType.BANNED);

        assertArrayEquals(expectedPlayed, actualPlayed);
        assertArrayEquals(expectedBanned, actualBanned);
    }

}
