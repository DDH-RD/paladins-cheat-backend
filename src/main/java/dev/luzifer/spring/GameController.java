package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.evaluation.BestChampForMapEvaluation;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(WebPath.GAME)
public class GameController {

    @Autowired
    private GameDao gameDao;

    @PostMapping(WebPath.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@RequestBody GameDto[] games) {
        gameDao.insert(games);
        Main.LOGGER.info("INSERTED " + games.length + " GAMES");
    }

    @GetMapping(WebPath.GET_COUNT)
    public @ResponseBody ResponseEntity<Integer> count() {
        Main.LOGGER.info("COUNTED " + gameDao.count() + " GAMES");
        return new ResponseEntity<>(gameDao.count(), HttpStatus.FOUND);
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_FOR_MAP)
    public @ResponseBody ResponseEntity<Map<ChampDto, Integer>> getBestChampForMap(@PathVariable String mapName) {

        Main.LOGGER.info("EVALUATED BEST CHAMP FOR MAP " + mapName);

        BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(mapName, gameDao);
        return new ResponseEntity<>(evaluation.evaluate(), HttpStatus.FOUND);
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP)
    public @ResponseBody ResponseEntity<Map<ChampDto, Integer>> getBestChampOfCategoryForMap(@PathVariable String mapName, @PathVariable String champCategory) {

        Main.LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory + " FOR MAP " + mapName);

        BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(mapName, gameDao);
        return new ResponseEntity<>(evaluation.evaluate(Integer.parseInt(champCategory)), HttpStatus.FOUND);
    }
}
