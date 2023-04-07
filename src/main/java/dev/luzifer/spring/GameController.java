package dev.luzifer.spring;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.evaluation.BestBanForMapEvaluation;
import dev.luzifer.data.evaluation.BestChampForMapEvaluation;
import dev.luzifer.data.evaluation.BestCounterChampEvaluation;
import dev.luzifer.data.match.info.GameDto;
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
import java.util.logging.Level;

@RestController
@RequestMapping(WebPath.GAME)
public class GameController {

    private static final Executor TASK_EXECUTOR =
            new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() / 4,
                    Runtime.getRuntime().availableProcessors(),
                    250L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(10000),
                    new ThreadFactoryBuilder()
                            .setUncaughtExceptionHandler((t, e) -> Main.LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + t.getName(), e))
                            .setDaemon(true)
                            .setNameFormat("Paladins Task-%d")
                            .build(),
                    new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private GameDao gameDao;

    @PostMapping(WebPath.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@RequestBody GameDto[] games) {
        TASK_EXECUTOR.execute(() -> gameDao.insert(games));
    }

    @GetMapping(WebPath.GET_COUNT)
    public @ResponseBody DeferredResult<ResponseEntity<Integer>> count(@RequestParam(required = false) boolean rankedOnly) {
        DeferredResult<ResponseEntity<Integer>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> gameDao.count(rankedOnly), TASK_EXECUTOR)
                .thenAccept(count -> deferredResult.setResult(new ResponseEntity<>(count, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampForMap(@RequestParam(required = false) boolean rankedOnly, @PathVariable String mapName) {

        Main.LOGGER.info("EVALUATED BEST CHAMP FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, rankedOnly);
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestChampOfCategoryForMap(@RequestParam(required = false) boolean rankedOnly, @PathVariable String mapName, @PathVariable int champCategory) {

        Main.LOGGER.info("EVALUATED BEST CHAMP OF CATEGORY " + champCategory + " FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestChampForMapEvaluation evaluation = new BestChampForMapEvaluation(finalMapName, gameDao, rankedOnly);
                    return evaluation.evaluate(champCategory);
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_COUNTER_CHAMP_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampForChamp(@RequestParam(required = false) boolean rankedOnly, @PathVariable int champId) {

        Main.LOGGER.info("EVALUATED BEST COUNTER CHAMP FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, rankedOnly);
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestCounterChampOfCategoryForChamp(@RequestParam(required = false) boolean rankedOnly, @PathVariable int champId, @PathVariable int champCategory) {

        Main.LOGGER.info("EVALUATED BEST COUNTER CHAMP OF CATEGORY " + champCategory + " FOR CHAMP " + champId);

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
                    BestCounterChampEvaluation evaluation = new BestCounterChampEvaluation(champId, gameDao, rankedOnly);
                    return evaluation.evaluate(champCategory);
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }

    @GetMapping(WebPath.GET_BEST_BAN_FOR_MAP)
    public @ResponseBody DeferredResult<ResponseEntity<Map<Integer, Integer>>> getBestBanForMap(@RequestParam(required = false) boolean rankedOnly, @PathVariable String mapName) {

        Main.LOGGER.info("EVALUATED BEST BAN FOR MAP " + mapName);
        mapName = mapName.replace("_", " ");

        DeferredResult<ResponseEntity<Map<Integer, Integer>>> deferredResult = new DeferredResult<>();
        String finalMapName = mapName;
        CompletableFuture.supplyAsync(() -> {
                    BestBanForMapEvaluation evaluation = new BestBanForMapEvaluation(finalMapName, gameDao, rankedOnly);
                    return evaluation.evaluate();
                }, TASK_EXECUTOR)
                .thenAccept(result -> deferredResult.setResult(new ResponseEntity<>(result, HttpStatus.FOUND)));
        return deferredResult;
    }
}
