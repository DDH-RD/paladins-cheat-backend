package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.DatabaseResult;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.spring.ApplicationAccessPoint;
import dev.luzifer.data.access.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.function.Supplier;
import java.util.logging.Level;

@RestController
@RequestMapping(ApplicationAccessPoint.GAME)
public class GameController {

    private static final DeferredResult UNAUTHORIZED_RESULT = new DeferredResult<ResponseEntity<?>>();

    static {
        UNAUTHORIZED_RESULT.setResult(new ResponseEntity<>("yeah no, dis is not your way sir!", HttpStatus.UNAUTHORIZED));
    }

    @Autowired
    private GameDao gameDao;

    @PostMapping(ApplicationAccessPoint.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@PathVariable String apiKey, @RequestBody GameDto[] gameDtos) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return;
        }

        TaskForce1.order(() -> {
            timing(() -> {
                int duplicated = 0;
                int success = 0;
                int error = 0;
                int notFound = 0;
                for (GameDto gameDto : gameDtos) {
                    DatabaseResult<Void> result = gameDao.saveGameData(gameDto);
                    switch (result.getDatabaseResultType()) {
                        case DUPLICATE -> duplicated++;
                        case SUCCESS -> success++;
                        case ERROR -> error++;
                        case NOT_FOUND -> notFound++;
                        default ->
                                throw new IllegalStateException("Unknown DatabaseResultType: " + result.getDatabaseResultType());
                    }
                }
                shootFormattedDebugMessage(duplicated, success, error, notFound);
            }, "Post games request with initially " + gameDtos.length + " games has been processed.");
        });
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT)
    public DeferredResult<ResponseEntity<?>> getGameCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalGameCount, "Game count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_CHAMPS)
    public DeferredResult<ResponseEntity<?>> getChampCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalChampCount, "Champ count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_ITEM_CRAFTS)
    public DeferredResult<ResponseEntity<?>> getItemCraftCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalItemCount, "Item craft count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_DECKS)
    public DeferredResult<ResponseEntity<?>> getDeckCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalDeckCount, "Deck count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_PLAYERS)
    public DeferredResult<ResponseEntity<?>> getPlayerCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalPlayerCount, "Player count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_BANNED_CHAMPS)
    public DeferredResult<ResponseEntity<?>> getBannedChampCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalBannedChampCount, "Banned champ count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_REGIONS)
    public DeferredResult<ResponseEntity<?>> getRegionCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalRegionCount, "Region count has been requested.");
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT_MAPS)
    public DeferredResult<ResponseEntity<?>> getMapCount(@PathVariable String apiKey) {
        return getCountResponse(apiKey, gameDao::getTotalMapCount, "Map count has been requested.");
    }

    private DeferredResult<ResponseEntity<?>> getCountResponse(String apiKey, Supplier<Integer> countSupplier, String logMessage) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return UNAUTHORIZED_RESULT;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(countSupplier.get(), HttpStatus.OK)),
                logMessage));
        return result;
    }

    private boolean couldNotVerifyApiKey(String key) {
        return !Webservice.getApiKey().equals(key);
    }

    private void timing(Runnable runnable, String message) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        runnable.run();

        stopWatch.stop();

        if(message == null) return;
        Webservice.REST_LOGGER.info(message + " (" + stopWatch.getTotalTimeMillis() + "ms)");
    }

    private void shootFormattedDebugMessage(int duplicated, int success, int error, int notFound) {

        Webservice.REST_LOGGER.info(success + " game(s) has been saved to the database.");

        if (notFound > 0) {
            Webservice.REST_LOGGER.warning(">     | " + notFound + " game(s) were not inserted");
        }

        if (error > 0) {
            Webservice.REST_LOGGER.warning(">             | " + error + " game(s) had errors");
        }

        if (duplicated > 0) {
            Webservice.REST_LOGGER.warning(">             | " + duplicated + " game(s) are duplicates");
        }
    }
}
