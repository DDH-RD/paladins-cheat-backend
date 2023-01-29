package dev.luzifer.data.match;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.info.GameInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("matchMapper")
public class MatchMapper implements Mapper<MatchId, GameInfo> {

    private final Map<MatchId, GameInfo> map = new HashMap<>();

    @Override
    public void delete(MatchId key) {
        map.remove(key);
    }

    @Override
    public void map(MatchId key, GameInfo value) {
        map.put(key, value);
    }

    @Override
    public void mapAll(Map<MatchId, GameInfo> map) {
        this.map.putAll(map);
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
        return map.getOrDefault(key, new GameInfo("INVALID GAMEINFO", 2, new long[] {-1}, null, null, -1, -1));
    }

    @Override
    public Map<MatchId, GameInfo> getMappings() {
        return map;
    }
}
