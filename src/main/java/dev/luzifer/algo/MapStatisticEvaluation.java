package dev.luzifer.algo;

import dev.luzifer.data.ChampMapper;
import dev.luzifer.data.gamestats.Champ;
import dev.luzifer.data.gamestats.GameMap;
import dev.luzifer.data.gamestats.champ.Category;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MapStatisticEvaluation {

    private static final Comparator<Champ> ALPHABETIC_ORDER = (c1, c2) -> {

        int res = String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName());

        if (res == 0)
            res = c1.getName().compareTo(c2.getName());

        return res;
    };

    private final ChampMapper champMapper;
    private final int returnAmount;

    public Champ[] evaluate(GameMap gameMap, Category category) {

        if(!champMapper.hasMapped(gameMap))
            return new Champ[0];

        Map<Champ, Integer> champsForMap = sortOutChamps(champMapper.getMapping(gameMap), category);
        return getAmountOfEntries(convertMapToSortedList(champsForMap));
    }

    public Champ[] evaluateAllTime(Category category) {

        if(!champMapper.hasAnyMapped())
            return new Champ[0];

        Map<Champ, Integer> champs = new HashMap<>();

        for(GameMap gameMap : champMapper.getMappings().keySet()) {
            Map<Champ, Integer> mapChamps = champMapper.getMapping(gameMap);
            for(Map.Entry<Champ, Integer> entry : mapChamps.entrySet()) {
                if(!champs.containsKey(entry.getKey()))
                    champs.put(entry.getKey(), entry.getValue());
                else
                    champs.put(entry.getKey(), champs.get(entry.getKey()) + entry.getValue());
            }
        }

        return getAmountOfEntries(convertMapToSortedList(sortOutChamps(champs, category)));
    }

    private Map<Champ, Integer> sortOutChamps(Map<Champ, Integer> map, Category category) {

        Set<Champ> set = map.keySet().stream()
                .filter(champ -> champ.getCategory() != category)
                .collect(Collectors.toSet());

        for(Champ champ : set)
            map.remove(champ);

        return map;
    }

    private List<Champ> convertMapToSortedList(Map<Champ, Integer> map) {

        List<Champ> sortedByUse = new ArrayList<>(map.keySet());

        sortedByUse.sort(ALPHABETIC_ORDER);
        sortedByUse.sort(Comparator.comparingInt(map::get));

        Collections.reverse(sortedByUse);

        return sortedByUse;
    }

    private Champ[] getAmountOfEntries(List<Champ> list) {
        return list.subList(0, Math.min(list.size(), returnAmount)).toArray(new Champ[0]);
    }
}
