package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.ChampMapper;
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

import java.util.Arrays;
import java.util.Map;

@Controller
public class PaladinsController {

    private final ChampMapper playedMapper = new ChampMapper();
    private final ChampMapper bannedMapper = new ChampMapper();

    @GetMapping(value = WebPath.GET_EVALUATED_PLAYED_SPECIFIC, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedPlayedForMap(@RequestParam(value = "map") GameMap map,
                                                                      @RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(playedMapper, 3).evaluate(map, category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = WebPath.GET_EVALUATED_PLAYED_TOTAL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedPlayedTotal(@RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(playedMapper, 3).evaluateAllTime(category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = WebPath.GET_EVALUATED_BANNED_SPECIFIC, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedBannedForMap(@RequestParam(value = "map") GameMap map,
                                                                      @RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(bannedMapper, 6).evaluate(map, category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = WebPath.GET_EVALUATED_BANNED_TOTAL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> evaluatedBannedTotal(@RequestParam(value = "category") Category category) {
        Champ[] result = new MapStatisticEvaluation(bannedMapper, 6).evaluateAllTime(category);
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @PostMapping(value = WebPath.POST_PLAYED_ENTRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postPlayed(@RequestParam(value = "map") GameMap map, @RequestBody Champ... champ) {
        Arrays.stream(champ).forEach(c -> playedMapper.map(map, Map.of(c, 1)));
    }

    @PostMapping(value = WebPath.POST_BANNED_ENTRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void postBanned(@RequestParam(value = "map") GameMap map, @RequestBody Champ... champ) {
        Arrays.stream(champ).forEach(c -> bannedMapper.map(map, Map.of(c, 1)));
    }
}
