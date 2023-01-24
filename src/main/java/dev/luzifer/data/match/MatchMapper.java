package dev.luzifer.data.match;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.info.GameInfo;

import java.util.HashMap;
import java.util.Map;

public class MatchMapper implements Mapper<MatchId, GameInfo> {

    private final Map<MatchId, GameInfo> map = new HashMap<>();

    @Override
    public void map(MatchId key, GameInfo value) {
        map.put(key, value);
    }

    @Override
    public boolean hasMapped(MatchId key) {
        return map.containsKey(key);
    }

    @Override
    public boolean hasAnyMapped() {
        return !map.isEmpty();
    }

    @Override
    public GameInfo getMapping(MatchId key) {
        return map.get(key);
    }

    @Override
    public Map<MatchId, GameInfo> getMappings() {
        return map;
    }
}