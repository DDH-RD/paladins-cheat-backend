package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.spring.ApplicationAccessPoint;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.distribution.TaskForce1;
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
import org.springframework.util.StopWatch;
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
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(ApplicationAccessPoint.GAME)
public class GameController {

    private static final DeferredResult UNAUTHORIZED_RESULT = new DeferredResult<ResponseEntity<?>>();

    static {
        UNAUTHORIZED_RESULT.setResult(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @Autowired
    private GameDao gameDao;

    @PostMapping(ApplicationAccessPoint.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@PathVariable String apiKey, @RequestBody ChampData[] champData) {

        if(couldNotVerifyApiKey(apiKey))
            return;

        TaskForce1.getTaskExecutor().execute(() -> gameDao.insertChampData(champData));
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT)
    public @ResponseBody DeferredResult<ResponseEntity<Integer>> count(@PathVariable String apiKey, @RequestParam(required = false) double season) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        DeferredResult<ResponseEntity<Integer>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Integer> count = new AtomicReference<>();
                    timing(() -> count.set(gameDao.count(season)));
                    return count.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_CHAMP_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampForMap(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable String mapName) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST CHAMP FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampOfCategoryForMap(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable String mapName, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory + " FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, season);
                        evaluationResult.set(evaluation.evaluate(champCategory));
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_COUNTER_CHAMP_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampForChamp(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST COUNTER CHAMP FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampOfCategoryForChamp(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable int champId, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST COUNTER CHAMP OF CATEGORY " + champCategory + " FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, season);
                        evaluationResult.set(evaluation.evaluate(champCategory));
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_BAN_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestBanForMap(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable String mapName) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST BAN FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestBanForMapEvaluation evaluation = new BestBanForMapEvaluation(finalMapName, gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_TALENT_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestTalentForChamp(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST TALENT FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestTalentForChampEvaluation evaluation = new BestTalentForChampEvaluation(champId, gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_DECK_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestDeckForChamp(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable int champId) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST DECK FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestDeckForChampEvaluation evaluation = new BestDeckForChampEvaluation(champId, gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChamp(@PathVariable String apiKey, @RequestParam(required = false) double season) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST CHAMP");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestChampEvaluation evaluation = new BestChampEvaluation(gameDao, season);
                        evaluationResult.set(evaluation.evaluate());
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(ApplicationAccessPoint.GET_BEST_CHAMP_OF_CATEGORY)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampOfCategory(@PathVariable String apiKey, @RequestParam(required = false) double season, @PathVariable int champCategory) {

        if(couldNotVerifyApiKey(apiKey))
            return UNAUTHORIZED_RESULT;

        Webservice.REST_LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            AtomicReference<Map<Integer, Integer>> evaluationResult = new AtomicReference<>();
                    timing(() -> {
                        BestChampEvaluation evaluation = new BestChampEvaluation(gameDao, season);
                        evaluationResult.set(evaluation.evaluate(champCategory));
                    });
                    return evaluationResult.get();
                }, TaskForce1.getTaskExecutor())
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    private boolean couldNotVerifyApiKey(String key) {
        return !Webservice.getApiKey().equals(key);
    }

    private void timing(Runnable runnable) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        runnable.run();
        stopWatch.stop();
        Webservice.REST_LOGGER.info("TIMING OF ACTION: " + stopWatch.getTotalTimeMillis() + "ms");
    }
}
