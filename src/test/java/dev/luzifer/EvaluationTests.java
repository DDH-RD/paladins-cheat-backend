package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.DataMapper;
import dev.luzifer.dto.ChampDto;
import dev.luzifer.dto.MapDto;
import dev.luzifer.enums.Category;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class EvaluationTests {

    @Test
    public void testEvaluation() {

        MapDto mapDto = new MapDto("CoolerMap");
        ChampDto maeve = new ChampDto("Maeve", "SUPPORT");
        ChampDto makoa = new ChampDto("Makoa", "SUPPORT");
        ChampDto khan = new ChampDto("Khan", "SUPPORT");
        ChampDto evie = new ChampDto("Evie", "SUPPORT");
        ChampDto ying = new ChampDto("Ying", "SUPPORT");

        DataMapper.map(mapDto, maeve);
        DataMapper.map(mapDto, maeve);
        DataMapper.map(mapDto, maeve);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto, khan);
        DataMapper.map(mapDto, khan);
        DataMapper.map(mapDto, khan);
        DataMapper.map(mapDto, evie);
        DataMapper.map(mapDto, evie);
        DataMapper.map(mapDto, ying);
        DataMapper.map(mapDto, ying);

        ChampDto[] expected = new ChampDto[] {makoa, maeve, khan};
        ChampDto[] actual = MapStatisticEvaluation.MAP_STATISTIC_EVALUATION.evaluate(mapDto, Category.SUPPORT);

        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluationAllTime() {

        MapDto mapDto = new MapDto("CoolerMap");
        MapDto mapDto1 = new MapDto("HahaLol");
        MapDto mapDto2 = new MapDto("Hurensonh");
        ChampDto maeve = new ChampDto("Maeve", "SUPPORT");
        ChampDto makoa = new ChampDto("Makoa", "SUPPORT");
        ChampDto khan = new ChampDto("Khan", "SUPPORT");
        ChampDto evie = new ChampDto("Evie", "SUPPORT");
        ChampDto ying = new ChampDto("Ying", "SUPPORT");

        DataMapper.map(mapDto, maeve);
        DataMapper.map(mapDto1, maeve);
        DataMapper.map(mapDto, maeve);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto1, makoa);
        DataMapper.map(mapDto1, makoa);
        DataMapper.map(mapDto2, makoa);
        DataMapper.map(mapDto, makoa);
        DataMapper.map(mapDto2, khan);
        DataMapper.map(mapDto, khan);
        DataMapper.map(mapDto, khan);
        DataMapper.map(mapDto1, evie);
        DataMapper.map(mapDto, evie);
        DataMapper.map(mapDto, ying);
        DataMapper.map(mapDto, ying);

        ChampDto[] expected = new ChampDto[] {makoa, maeve, khan};
        ChampDto[] actual = MapStatisticEvaluation.MAP_STATISTIC_EVALUATION.evaluateAllTime(Category.SUPPORT);

        System.out.println(Arrays.toString(expected));
        System.out.println(Arrays.toString(actual));

        assertEquals(expected, actual);
    }
}
