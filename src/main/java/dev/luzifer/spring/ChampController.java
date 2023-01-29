package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.algo.evaluation.ChampStatisticEvaluation;
import dev.luzifer.data.match.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebPath.CHAMP)
public class ChampController {

    @Autowired
    private MatchMapper matchMapper;

    @GetMapping(value = WebPath.GET_CHAMP_COUNTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<long[]> getChampCounter(@PathVariable long id) {
        
        Main.LOGGER.info("RECEIVED GET REQUEST FOR CHAMP:" + id + " COUNTER");
        
        return new ResponseEntity<>(new ChampStatisticEvaluation(matchMapper)
                .evaluateCounterFor(id), HttpStatus.OK);
    }
    
    @GetMapping(value = WebPath.GET_CHAMP_WINGMAN, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<long[]> getChampWingman(@PathVariable long id) {
        
        Main.LOGGER.info("RECEIVED GET REQUEST FOR CHAMP:" + id + " WINGMAN");
        
        return new ResponseEntity<>(new ChampStatisticEvaluation(matchMapper)
                .evaluateWingmanFor(id), HttpStatus.OK);
    }
}
