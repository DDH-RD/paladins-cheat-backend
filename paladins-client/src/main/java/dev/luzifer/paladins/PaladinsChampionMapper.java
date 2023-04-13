package dev.luzifer.paladins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaladinsChampionMapper {

    private final Map<Integer, PaladinsChampion> champions = new HashMap<>();

    public PaladinsChampionMapper(List<PaladinsChampion> champions) {
        for (PaladinsChampion champion : champions) {
            this.champions.put(champion.getId(), champion);
        }
    }

    public PaladinsChampion getChampion(int id) {
        return champions.get(id);
    }

    public PaladinsChampion getChampionByName(String name) {
        for (PaladinsChampion champion : champions.values()) {
            if (champion.getName().equalsIgnoreCase(name)) {
                return champion;
            }
        }
        return null;
    }

    public Map<Integer, PaladinsChampion> getChampions() {
        return champions;
    }
}
