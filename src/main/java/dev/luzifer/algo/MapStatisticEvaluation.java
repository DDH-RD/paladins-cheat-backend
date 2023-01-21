package dev.luzifer.algo;

import dev.luzifer.data.DataMapper;
import dev.luzifer.dto.ChampDto;
import dev.luzifer.dto.MapDto;
import dev.luzifer.enums.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MapStatisticEvaluation {

    public static final MapStatisticEvaluation MAP_STATISTIC_EVALUATION = new MapStatisticEvaluation();

    private static final Comparator<ChampDto> ALPHABETIC_ORDER = (c1, c2) -> {
        int res = String.CASE_INSENSITIVE_ORDER.compare(c1.getName(), c2.getName());
        if (res == 0) {
            res = c1.getName().compareTo(c2.getName());
        }
        return res;
    };

    public ChampDto[] evaluate(MapDto mapDto, Category category) {

        if(!DataMapper.hasDataFor(mapDto))
            return new ChampDto[0];

        Map<ChampDto, Integer> champsForMap = sortOutChamps(DataMapper.getEntriesForMap(mapDto), category);
        return getFirstThreeEntries(convertMapToSortedList(champsForMap));
    }

    public ChampDto[] evaluateAllTime(Category category) {

        if(!DataMapper.hasData())
            return new ChampDto[0];

        Map<ChampDto, Integer> champs = new HashMap<>();

        for(MapDto mapDto : DataMapper.getMaps()) {
            Map<ChampDto, Integer> mapChamps = DataMapper.getEntriesForMap(mapDto);
            for(Map.Entry<ChampDto, Integer> entry : mapChamps.entrySet()) {
                if(!champs.containsKey(entry.getKey()))
                    champs.put(entry.getKey(), entry.getValue());
                else
                    champs.put(entry.getKey(), champs.get(entry.getKey()) + entry.getValue());
            }
        }

        return getFirstThreeEntries(convertMapToSortedList(sortOutChamps(champs, category)));
    }

    private Map<ChampDto, Integer> sortOutChamps(Map<ChampDto, Integer> map, Category category) {

        Set<ChampDto> set = map.keySet().stream()
                .filter(champ -> !champ.getCategory().equalsIgnoreCase(category.name()))
                .collect(Collectors.toSet());

        for(ChampDto champDto : set)
            map.remove(champDto);

        return map;
    }

    private List<ChampDto> convertMapToSortedList(Map<ChampDto, Integer> map) {

        List<ChampDto> sortedByUse = new ArrayList<>(map.keySet());

        sortedByUse.sort(ALPHABETIC_ORDER);
        sortedByUse.sort(Comparator.comparingInt(map::get));

        /*
         * joah, die order hier ist irgendwie fucked.
         * eigentlich sollte das nach der gewichtung gehen
         * wie oft dieser champ auf der map gespielt wurde und dann alphabetisch sortiert,
         * aber irgendwie will der nicht und deswegen ist das reverse alphabetisch sortiert haha lol
         */

        Collections.reverse(sortedByUse);

        return sortedByUse;
    }

    private ChampDto[] getFirstThreeEntries(List<ChampDto> list) {
        return list.subList(0, Math.min(list.size(), 3)).toArray(new ChampDto[0]);
    }

    private MapStatisticEvaluation() {
    }
}
