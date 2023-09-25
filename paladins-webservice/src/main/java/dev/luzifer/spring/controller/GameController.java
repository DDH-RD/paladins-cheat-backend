package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
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
    
    private long lastUpdate = System.currentTimeMillis();

    @PostMapping(ApplicationAccessPoint.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postGames(@PathVariable String apiKey, @RequestBody GameDto[] gameDtos) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return;
        }

        TaskForce1.order(() -> {
            timing(() -> {
                for (GameDto gameDto : gameDtos) {
                    gameDao.saveGameData(gameDto);
                }
            }, gameDtos.length + " games has been saved to the database. " + getSuffix());
        });
    }

    @GetMapping(ApplicationAccessPoint.GET_COUNT)
    public DeferredResult<ResponseEntity<?>> getGameCount(@PathVariable String apiKey) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return UNAUTHORIZED_RESULT;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(()
                -> result.setResult(new ResponseEntity<>(gameDao.getTotalGameCount(), HttpStatus.OK)),
                "Game count has been requested."));
        return result;
    }

    private boolean couldNotVerifyApiKey(String key) {
        return !Webservice.getApiKey().equals(key);
    }
    
    private String getSuffix() {
        if(System.currentTimeMillis() - lastUpdate > 300000) { // 5 min
            lastUpdate = System.currentTimeMillis();
            return "(Datenstand:" + gameDao.getTotalGameCount() + ")";
        }
        return "";
    }

    private void timing(Runnable runnable, String message) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        runnable.run();

        stopWatch.stop();
        Webservice.REST_LOGGER.info(message + " (" + stopWatch.getTotalTimeMillis() + "ms)");
    }
}
