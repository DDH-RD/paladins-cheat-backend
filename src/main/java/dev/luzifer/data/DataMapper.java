package dev.luzifer.data;

import dev.luzifer.dto.ChampDto;
import dev.luzifer.dto.MapDto;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class DataMapper {

    private static final MappedDao MAPPED_DAO = new MappedDao();
    private static final Map<MapDto, Map<ChampDto, Integer>> MAP = new HashMap<>();

    public static void map(MapDto mapDto, ChampDto... champDto) {

        Map<ChampDto, Integer> champMap = MAP.getOrDefault(mapDto, new HashMap<>());
        for(ChampDto champ : champDto) {

            if(!champMap.containsKey(champ))
                champMap.put(champ, 0);

            champMap.put(champ, champMap.get(champ) + 1);
        }

        MAP.put(mapDto, champMap);
    }

    public static void save() {
        MAPPED_DAO.saveAll();
    }

    public static void delete() {
        MAPPED_DAO.deleteAll();
    }

    public static void init() {
        MAP.clear();
        MAP.putAll(MAPPED_DAO.fetchAll());
    }

    public static MapDto getMapByName(String name) {
        return MAP.keySet().stream().filter(mapDto -> mapDto.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Map<ChampDto, Integer> getEntriesForMap(MapDto mapDto) {
        return MAP.get(mapDto);
    }

    public static boolean hasData() {
        return !MAP.isEmpty();
    }

    public static boolean hasDataFor(MapDto mapDto) {
        return MAP.get(mapDto) != null;
    }

    public static Set<MapDto> getMaps() {
        return MAP.keySet();
    }

}
