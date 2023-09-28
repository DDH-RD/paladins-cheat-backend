package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.async.DeferredResult;

public abstract class AbstractController {

    protected static final DeferredResult UNAUTHORIZED_RESULT = new DeferredResult<ResponseEntity<?>>();

    static {
        UNAUTHORIZED_RESULT.setResult(new ResponseEntity<>("yeah no, dis is not your way sir!", HttpStatus.UNAUTHORIZED));
    }

    @Autowired
    protected GameDao gameDao;

    protected boolean couldNotVerifyApiKey(String key) {
        return !Webservice.getApiKey().equals(key);
    }

    protected void timing(Runnable runnable, String message) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        runnable.run();

        stopWatch.stop();

        if(message == null) return;
        Webservice.REST_LOGGER.info(message + " (" + stopWatch.getTotalTimeMillis() + "ms)");
    }
}
