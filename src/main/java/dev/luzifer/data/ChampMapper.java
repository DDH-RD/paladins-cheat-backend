package dev.luzifer.data;

import dev.luzifer.data.gamestats.ChampId;
import dev.luzifer.data.gamestats.ChampInfo;

import java.util.HashMap;
import java.util.Map;

public class ChampMapper implements Mapper<ChampId, ChampInfo> {

    private final Map<ChampId, ChampInfo> map = new HashMap<>();

    @Override
    public void map(ChampId key, ChampInfo value) {
        map.put(key, value);
    }

    @Override
    public boolean hasMapped(ChampId key) {
        return map.containsKey(key);
    }

    @Override
    public boolean hasAnyMapped() {
        return !map.isEmpty();
    }

    @Override
    public ChampInfo getMapping(ChampId key) {
        return map.get(key);
    }

    @Override
    public Map<ChampId, ChampInfo> getMappings() {
        return map;
    }
}
