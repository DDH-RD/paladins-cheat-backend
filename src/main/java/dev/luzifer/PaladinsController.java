package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.GameMapper;
import dev.luzifer.data.gamestats.Champ;
import dev.luzifer.data.gamestats.GameMap;
import dev.luzifer.data.gamestats.champ.Category;
import dev.luzifer.json.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class PaladinsController {

    private final GameMapper gameMapper = new GameMapper();

    @GetMapping(value = WebPath.GET_EVALUATED_SPECIFIC, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedForMap(@RequestParam(value = "map") GameMap map,
                                                                @RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(gameMapper).evaluate(map, category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = WebPath.GET_EVALUATED_TOTAL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedTotal(@RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(gameMapper).evaluateAllTime(category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @PostMapping(value = WebPath.POST_ENTRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void post(@RequestParam(value = "map") GameMap map, @RequestBody Champ... champ) {
        for(Champ ch : champ)
            gameMapper.map(map, Map.of(ch, 1)); // das ist eigentlich so dumm - alles für clean generics
    }
}
