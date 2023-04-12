package dev.luzifer.data.cache;

import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChampDataCache {

    private final List<ChampData> entries = new ArrayList<>();
    private final Map<Integer, ChampData> entriesByMatchType = new HashMap<>();
    private final Map<Integer, ChampData> entriesByChampId = new HashMap<>();
    private final Map<String, ChampData> entriesByMapName = new HashMap<>();
    private final Map<Integer, ChampData> entriesByCategoryId = new HashMap<>();
    private final Map<BiContainer<String, Integer>, ChampData> entriesByMapNameAndCategoryId = new HashMap<>();

    private long lastUpdate = 0;

    public void add(ChampData champData) {

        entries.add(champData);
        entriesByMatchType.put(champData.getRanked(), champData);
        entriesByChampId.put(champData.getChampId(), champData);
        entriesByMapName.put(champData.getMapName(), champData);
        entriesByCategoryId.put(champData.getCategoryId(), champData);
        entriesByMapNameAndCategoryId.put(new BiContainer<>(champData.getMapName(), champData.getCategoryId()), champData);

        lastUpdate = Instant.now().toEpochMilli();
    }

    public List<ChampData> getEntries() {
        return entries;
    }

    public ChampData getEntryByMatchType(GameController.MatchType matchType) {
        int ranked = matchType == GameController.MatchType.RANKED ? 1 : 0;
        return entriesByMatchType.get(ranked);
    }

    public ChampData getEntryByChampId(int champId) {
        return entriesByChampId.get(champId);
    }

    public ChampData getEntryByMapName(String mapName) {
        return entriesByMapName.get(mapName);
    }

    public ChampData getEntryByCategoryId(int categoryId) {
        return entriesByCategoryId.get(categoryId);
    }

    public ChampData getEntryByMapNameAndCategoryId(String mapName, int categoryId) {
        return entriesByMapNameAndCategoryId.get(new BiContainer<>(mapName, categoryId));
    }

    @EqualsAndHashCode
    private static class BiContainer<T, V> {

        private final T key;
        private final V value;

        public BiContainer(T key, V value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

}
