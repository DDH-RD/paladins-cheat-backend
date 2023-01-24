package dev.luzifer.algo;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.gamestats.GameInfo;
import dev.luzifer.data.gamestats.MatchId;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MatchStatisticEvaluation {

    private final Mapper<MatchId, GameInfo> matchMapper;

    public long[] evaluate(ResultType resultType) {

        Map<Long, Integer> counter = new HashMap<>();

        for(MatchId matchId : matchMapper.getMappings().keySet()) {

            GameInfo gameInfo = matchMapper.getMapping(matchId);
            long[] ids = resultType == ResultType.PLAYED ? gameInfo.getPlayedChampIds() : gameInfo.getBannedChampIds();

            evaluate(counter, ids);
        }

        return sortAndLimit(counter);
    }

    private long[] sortAndLimit(Map<Long, Integer> counter) {

        List<Long> sortedList = new ArrayList<>(counter.keySet());
        sortedList.sort(Comparator.comparingInt(counter::get));

        Collections.reverse(sortedList);

        return sortedList.stream().mapToLong(id -> id).toArray();
    }

    private void evaluate(Map<Long, Integer> counter, long[] ids) {

        int index = 0;
        while(index < ids.length) {

            long id = ids[index++];
            if(!counter.containsKey(id))
                counter.put(id, 1);
            else
                counter.put(id, counter.get(id) + 1);
        }
    }
}
