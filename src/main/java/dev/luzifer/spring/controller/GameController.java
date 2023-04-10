package dev.luzifer.spring.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.evaluation.BestBanForMapEvaluation;
import dev.luzifer.data.evaluation.BestChampEvaluation;
import dev.luzifer.data.evaluation.BestChampForMapEvaluation;
import dev.luzifer.data.evaluation.BestCounterChampEvaluation;
import dev.luzifer.data.evaluation.BestDeckForChampEvaluation;
import dev.luzifer.data.evaluation.BestTalentForChampEvaluation;
import dev.luzifer.data.match.info.ChampData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(WebPath.API_KEY + WebPath.GAME)
public class GameController {

    private static final Executor TASK_EXECUTOR =
            new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() / 4,
                    Runtime.getRuntime().availableProcessors(),
                    250L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(10000),
                    new ThreadFactoryBuilder()
                            .setUncaughtExceptionHandler((t, e) -> e.printStackTrace())
                            .setDaemon(true)
                            .setNameFormat("Paladins Task-%d")
                            .build(),
                    new ThreadPoolExecutor.AbortPolicy());

    private static final DeferredResult UNAUTHORIZED_RESULT = new DeferredResult<ResponseEntity<?>>();

    static {
        UNAUTHORIZED_RESULT.setResult(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @Autowired
    private GameDao gameDao;

    @PostMapping(WebPath.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@PathVariable String apiKey, @RequestBody ChampData[] champData) {

        if(couldNotVerifyApiKey(apiKey))
            return;

        TASK_EXECUTOR.execute(() -> gameDao.insertChampData(champData));
    }

    @GetMapping(WebPath.GET_COUNT)
    public @ResponseBody DeferredResult<ResponseEntity<Integer>> count(@PathVariable String apiKey, @RequestParam String matchType) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        DeferredResult<ResponseEntity<Integer>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> gameDao.count(MatchType.valueOf(matchType)), TASK_EXECUTOR)
                .thenAccept(count -> deferredResult.setResult(new ResponseEntity<>(count, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampForMap(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable String mapName) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST CHAMP FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampOfCategoryForMap(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable String mapName, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory + " FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate(champCategory);
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_COUNTER_CHAMP_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampForChamp(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST COUNTER CHAMP FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampOfCategoryForChamp(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable int champId, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST COUNTER CHAMP OF CATEGORY " + champCategory + " FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate(champCategory);
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_BAN_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestBanForMap(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable String mapName) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST BAN FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestBanForMapEvaluation evaluation = new BestBanForMapEvaluation(finalMapName, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_TALENT_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestTalentForChamp(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST TALENT FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestTalentForChampEvaluation evaluation = new BestTalentForChampEvaluation(champId, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_DECK_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestDeckForChamp(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST DECK FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestDeckForChampEvaluation evaluation = new BestDeckForChampEvaluation(champId, gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChamp(@PathVariable String apiKey, @RequestParam String matchType) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST CHAMP");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestChampEvaluation evaluation = new BestChampEvaluation(gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_OF_CATEGORY)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampOfCategory(@PathVariable String apiKey, @RequestParam String matchType, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Main.REST_LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestChampEvaluation evaluation = new BestChampEvaluation(gameDao, MatchType.valueOf(matchType));
                    return evaluation.evaluate(champCategory);
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    private boolean couldNotVerifyApiKey(String key) {
        return !Main.getApiKey().equals(key);
    }

    public enum MatchType {
        ALL,
        RANKED, 
        CASUAL
    }
}
