package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.algo.evaluation.ChampStatisticEvaluation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(WebPath.CHAMP)
public class ChampController {
    
    @GetMapping(value = WebPath.GET_CHAMP_COUNTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Long> getChampCounter(@PathVariable long id) {
        
        Main.LOGGER.info("RECEIVED GET REQUEST FOR CHAMP:" + id + " COUNTER");
        
        return new ResponseEntity<>(new ChampStatisticEvaluation(Application.MATCH_ID_GAME_INFO_MAPPER)
                .evaluateCounterFor(id), HttpStatus.OK);
    }
    
    @GetMapping(value = WebPath.GET_CHAMP_WINGMAN, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Long> getChampWingman(@PathVariable long id) {
        
        Main.LOGGER.info("RECEIVED GET REQUEST FOR CHAMP:" + id + " WINGMAN");
        
        return new ResponseEntity<>(new ChampStatisticEvaluation(Application.MATCH_ID_GAME_INFO_MAPPER)
                .evaluateWingmanFor(id), HttpStatus.OK);
    }
}
