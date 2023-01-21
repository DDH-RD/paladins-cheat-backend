package dev.luzifer.data;

import dev.luzifer.dto.ChampDto;
import dev.luzifer.dto.MapDto;

import java.util.Map;

/* Mapped = MapDto + (ChampDto...) */
class MappedDao {

    public void saveAll() {
        throw new UnsupportedOperationException();
    }

    public Map<MapDto, Map<ChampDto, Integer>> fetchAll() {
        throw new UnsupportedOperationException();
    }

    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    MappedDao() {
    }
}
