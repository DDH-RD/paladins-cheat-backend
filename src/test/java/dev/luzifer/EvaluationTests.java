package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.GameMapper;
import dev.luzifer.data.gamestats.Champ;
import dev.luzifer.data.gamestats.GameMap;
import dev.luzifer.data.gamestats.champ.Category;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EvaluationTests {

    @Test
    public void testMerge() {

        GameMapper gameMapper = new GameMapper();

        GameMap key = new GameMap("KeyMap");
        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_FLANKER);

        Map<Champ, Integer> map = new HashMap<>();
        map.put(maeve, 2);

        gameMapper.map(key, map);
        gameMapper.map(key, map);

        assertEquals(4, (int) gameMapper.getMapping(key).get(maeve));
    }

    @Test
    public void testEvaluation() {

        GameMapper gameMapper = new GameMapper();

        GameMap gameMap = new GameMap("CoolerMap");
        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_SUPPORT);
        Champ makoa = new Champ(2, "Makoa", Category.PALADINS_SUPPORT);
        Champ khan = new Champ(3, "Khan", Category.PALADINS_SUPPORT);
        Champ evie = new Champ(4, "Evie", Category.PALADINS_SUPPORT);
        Champ ying = new Champ(5, "Ying", Category.PALADINS_SUPPORT);

        Map<Champ, Integer> maeveMap = new HashMap<>();
        maeveMap.put(maeve, 1);

        Map<Champ, Integer> makoaMap = new HashMap<>();
        makoaMap.put(makoa, 2);

        Map<Champ, Integer> khanMap = new HashMap<>();
        khanMap.put(khan, 4);

        Map<Champ, Integer> yingMap = new HashMap<>();
        yingMap.put(ying, 1);

        Map<Champ, Integer> evieMap = new HashMap<>();
        evieMap.put(evie, 7);

        gameMapper.map(gameMap, maeveMap);
        gameMapper.map(gameMap, makoaMap);
        gameMapper.map(gameMap, khanMap);
        gameMapper.map(gameMap, evieMap);
        gameMapper.map(gameMap, yingMap);

        Champ[] expected = new Champ[] {evie, khan, makoa};
        Champ[] actual = new MapStatisticEvaluation(gameMapper).evaluate(gameMap, Category.PALADINS_SUPPORT);

        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluationAllTime() {

        GameMapper gameMapper = new GameMapper();

        GameMap gameMap = new GameMap("CoolerMap");
        GameMap gameMap1 = new GameMap("HahaLol");
        GameMap gameMap2 = new GameMap("Hurensonh");
        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_SUPPORT);
        Champ makoa = new Champ(2, "Makoa", Category.PALADINS_SUPPORT);
        Champ khan = new Champ(3, "Khan", Category.PALADINS_SUPPORT);
        Champ evie = new Champ(4, "Evie", Category.PALADINS_SUPPORT);
        Champ ying = new Champ(5, "Ying", Category.PALADINS_SUPPORT);

        Map<Champ, Integer> maeveMap = new HashMap<>();
        maeveMap.put(maeve, 1);

        Map<Champ, Integer> makoaMap = new HashMap<>();
        maeveMap.put(makoa, 2);

        Map<Champ, Integer> khanMap = new HashMap<>();
        maeveMap.put(khan, 4);

        Map<Champ, Integer> yingMap = new HashMap<>();
        maeveMap.put(ying, 1);

        Map<Champ, Integer> evieMap = new HashMap<>();
        maeveMap.put(evie, 7);

        gameMapper.map(gameMap, maeveMap);
        gameMapper.map(gameMap1, makoaMap);
        gameMapper.map(gameMap, khanMap);
        gameMapper.map(gameMap2, evieMap);
        gameMapper.map(gameMap, yingMap);

        Champ[] expected = new Champ[] {evie, khan, makoa};
        Champ[] actual = new MapStatisticEvaluation(gameMapper).evaluateAllTime(Category.PALADINS_SUPPORT);

        assertEquals(expected, actual);
    }
}
