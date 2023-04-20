package dev.luzifer.data.access;

import dev.luzifer.data.match.info.ChampData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseCache {
    
    private final Set<ChampData> champDataSet = new HashSet<>();
    
    // indexing
    private final Map<String, Set<ChampData>> champDataByMapNameMap = new HashMap<>();
    private final Map<Integer, Set<ChampData>> champDataByChampIdMap = new HashMap<>();
    private final Map<Integer, Set<ChampData>> champDataByMatchIdMap = new HashMap<>();
    private final Map<Integer, Set<ChampData>> champDataByCategoryIdMap = new HashMap<>();
    private final Map<BiContainer<String, Integer>, Set<ChampData>> champDataByMapNameAndCategoryIdMap = new HashMap<>();
    private final Map<BiContainer<Integer, Integer>, Set<ChampData>> champDataByMatchIdAndCategoryIdMap = new HashMap<>();
    
    public void init(List<ChampData> list) {
        list.forEach(this::addChampData);
    }
    
    public void update(ChampData champData) {
        addChampData(champData);
    }
    
    public Set<ChampData> getChampDataForMap(String mapName) {
        return champDataByMapNameMap.get(mapName);
    }
    
    public Set<ChampData> getChampDataForChamp(int champId) {
        return champDataByChampIdMap.get(champId);
    }
    
    public Set<ChampData> getChampDataForMatch(int matchId) {
        return champDataByMatchIdMap.get(matchId);
    }
    
    public Set<ChampData> getChampDataForCategory(int categoryId) {
        return champDataByCategoryIdMap.get(categoryId);
    }
    
    public Set<ChampData> getChampDataForMapOfCategory(String mapName, int categoryId) {
        return champDataByMapNameAndCategoryIdMap.get(new BiContainer<>(mapName, categoryId));
    }
    
    public Set<ChampData> getChampDataForMatchOfCategory(int matchId, int categoryId) {
        return champDataByMatchIdAndCategoryIdMap.get(new BiContainer<>(matchId, categoryId));
    }
    
    public Set<ChampData> getChampDataSet() {
        return champDataSet;
    }
    
    private void addChampData(ChampData champData) {
        champDataSet.add(champData);
        
        champDataByMapNameMap.computeIfAbsent(champData.getMapName(), k -> new HashSet<>()).add(champData);
        champDataByChampIdMap.computeIfAbsent(champData.getChampId(), k -> new HashSet<>()).add(champData);
        champDataByMatchIdMap.computeIfAbsent(champData.getMatchId(), k -> new HashSet<>()).add(champData);
        champDataByCategoryIdMap.computeIfAbsent(champData.getCategoryId(), k -> new HashSet<>()).add(champData);
        champDataByMapNameAndCategoryIdMap.computeIfAbsent(new BiContainer<>(champData.getMapName(), champData.getCategoryId()), k -> new HashSet<>()).add(champData);
        champDataByMatchIdAndCategoryIdMap.computeIfAbsent(new BiContainer<>(champData.getMatchId(), champData.getCategoryId()), k -> new HashSet<>()).add(champData);
    }
    
    private static class BiContainer<K, KEY> {
        
        private final K key;
        private final KEY key2;
        
        public BiContainer(K key, KEY key2) {
            this.key = key;
            this.key2 = key2;
        }
        
        public K getKey() {
            return key;
        }
        
        public KEY getKey2() {
            return key2;
        }
    }
}
