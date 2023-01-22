package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.ChampMapper;
import dev.luzifer.data.gamestats.Champ;
import dev.luzifer.data.gamestats.GameMap;
import dev.luzifer.data.gamestats.champ.Category;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EvaluationTests {

    @Test
    public void testMerge() {

        ChampMapper champMapper = new ChampMapper();

        GameMap key = new GameMap("KeyMap");
        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_FLANKER);

        champMapper.map(key, Map.of(maeve, 2));
        champMapper.map(key, Map.of(maeve, 2));

        assertEquals(4, (int) champMapper.getMapping(key).get(maeve));
    }

    @Test
    public void testEvaluation() {

        ChampMapper champMapper = new ChampMapper();

        GameMap gameMap = new GameMap("CoolerMap");
        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_SUPPORT);
        Champ makoa = new Champ(2, "Makoa", Category.PALADINS_SUPPORT);
        Champ khan = new Champ(3, "Khan", Category.PALADINS_SUPPORT);
        Champ evie = new Champ(4, "Evie", Category.PALADINS_SUPPORT);
        Champ ying = new Champ(5, "Ying", Category.PALADINS_SUPPORT);

        champMapper.map(gameMap, Map.of(maeve, 1));
        champMapper.map(gameMap, Map.of(makoa, 2));
        champMapper.map(gameMap, Map.of(khan, 4));
        champMapper.map(gameMap, Map.of(ying, 1));
        champMapper.map(gameMap, Map.of(evie, 7));

        Champ[] expected = new Champ[] {evie, khan, makoa};
        Champ[] actual = new MapStatisticEvaluation(champMapper).evaluate(gameMap, Category.PALADINS_SUPPORT);

        assertEquals(expected, actual);
    }

    @Test
    public void testEvaluationAllTime() {

        ChampMapper champMapper = new ChampMapper();

        GameMap gameMap = new GameMap("CoolerMap");
        GameMap gameMap1 = new GameMap("HahaLol");
        GameMap gameMap2 = new GameMap("Hurensonh");

        Champ maeve = new Champ(1, "Maeve", Category.PALADINS_SUPPORT);
        Champ makoa = new Champ(2, "Makoa", Category.PALADINS_SUPPORT);
        Champ khan = new Champ(3, "Khan", Category.PALADINS_SUPPORT);
        Champ evie = new Champ(4, "Evie", Category.PALADINS_SUPPORT);
        Champ ying = new Champ(5, "Ying", Category.PALADINS_SUPPORT);

        champMapper.map(gameMap, Map.of(maeve, 1));
        champMapper.map(gameMap2, Map.of(makoa, 2));
        champMapper.map(gameMap, Map.of(khan, 4));
        champMapper.map(gameMap1, Map.of(ying, 1));
        champMapper.map(gameMap, Map.of(evie, 7));

        Champ[] expected = new Champ[] {evie, khan, makoa};
        Champ[] actual = new MapStatisticEvaluation(champMapper).evaluateAllTime(Category.PALADINS_SUPPORT);

        assertEquals(expected, actual);
    }
}
