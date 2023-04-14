package dev.luzifer.paladins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaladinsChampionMapper {

    private final Map<Integer, PaladinsChampion> championMap = new HashMap<>();

    public PaladinsChampionMapper(List<PaladinsChampion> champions) {
        champions.forEach(champion -> championMap.put(champion.getId(), champion));
    }

    public PaladinsChampion getChampion(int id) {
        return championMap.get(id);
    }

    public PaladinsChampion getChampionByName(String name) {
        return championMap.values().stream()
                .filter(champion -> champion.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Map<Integer, PaladinsChampion> getChampionMap() {
        return championMap;
    }
}
