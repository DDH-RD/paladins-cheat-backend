package dev.luzifer;

import dev.luzifer.algo.MapStatisticEvaluation;
import dev.luzifer.data.DataMapper;
import dev.luzifer.dto.ChampDto;
import dev.luzifer.enums.Category;
import dev.luzifer.json.JsonUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * Das mit Param mapName könnte richtig garbage sein und alles zerstört haben was intern mit ner map zu tun hat.
 * bzw. die auto generation eines neuen map objekts
 */
@Controller
public class PaladinsController {

    @GetMapping(value = WebPath.PALADINS_EVALUATED_FOR_MAP)
    public @ResponseBody ResponseEntity<String> evaluatedForMap(@Param(value = "mapName") String mapName,
                                                                @Param(value = "category") String category) {
        ChampDto[] result = MapStatisticEvaluation.MAP_STATISTIC_EVALUATION
                .evaluate(DataMapper.getMapByName(mapName), Category.valueOf(category));
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @GetMapping(value = WebPath.PALADINS_EVALUATED_TOTAL)
    public @ResponseBody ResponseEntity<String> evaluatedTotal(@Param(value = "category") String category) {
        ChampDto[] result = MapStatisticEvaluation.MAP_STATISTIC_EVALUATION.evaluateAllTime(Category.valueOf(category));
        return new ResponseEntity<>(JsonUtil.toJson(result), HttpStatus.OK);
    }

    @PostMapping(value = WebPath.PALADINS_RECEIVE_ENTRY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void post(@Param(value = "mapName") String mapName, @RequestBody ChampDto... champ) {
        DataMapper.map(DataMapper.getMapByName(mapName), champ);
    }
}
