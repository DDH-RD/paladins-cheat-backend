package dev.luzifer.data;

import dev.luzifer.data.gamestats.Champ;
import dev.luzifer.data.gamestats.GameMap;

import java.util.HashMap;
import java.util.Map;

/* das ist eigentlich so dumm. könnte locker <GameMap, Champ> sein */
public class ChampMapper implements Mapper<GameMap, Map<Champ, Integer>> {

    private final Map<GameMap, Map<Champ, Integer>> map = new HashMap<>();

    @Override
    public void map(GameMap key, Map<Champ, Integer> value) {

        Map<Champ, Integer> keyMap = getOrCreateMap(key);
        merge(keyMap, value);

        map.put(key, keyMap);
    }

    @Override
    public boolean hasMapped(GameMap key) {
        return map.get(key) != null;
    }

    @Override
    public boolean hasAnyMapped() {
        return !map.isEmpty();
    }

    @Override
    public Map<Champ, Integer> getMapping(GameMap key) {
        return map.get(key);
    }

    @Override
    public Map<GameMap, Map<Champ, Integer>> getMappings() {
        return map;
    }

    private Map<Champ, Integer> getOrCreateMap(GameMap key) {
        if(map.containsKey(key))
            return map.get(key);
        else
            return new HashMap<>();
    }

    private void merge(Map<Champ, Integer> into, Map<Champ, Integer> from) {
        for (Map.Entry<Champ, Integer> entry : from.entrySet()) {
            Champ key = entry.getKey();
            Integer value = entry.getValue();
            if (into.containsKey(key)) {
                into.put(key, into.get(key) + value);
            } else {
                into.put(key, value);
            }
        }
    }
}
