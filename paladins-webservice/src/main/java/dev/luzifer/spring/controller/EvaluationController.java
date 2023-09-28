package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.data.evaluation.BestBansEvaluation;
import dev.luzifer.spring.ApplicationAccessPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.logging.Level;

@RestController
@RequestMapping(ApplicationAccessPoint.GET_EVALUATION)
public class EvaluationController extends AbstractController {

    @GetMapping(ApplicationAccessPoint.GET_EVALUATION_BEST_BANS)
    public DeferredResult<ResponseEntity<?>> getBestBans(@PathVariable String apiKey) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return UNAUTHORIZED_RESULT;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(new BestBansEvaluation(gameDao)
                        .evaluate().getResult(),
                        HttpStatus.OK)),
                "Best bans have been requested."));
        return result;
    }

    @GetMapping(ApplicationAccessPoint.GET_EVALUATION_BEST_BANS_FOR_MAP)
    public DeferredResult<ResponseEntity<?>> getBestBansForMap(@PathVariable String apiKey, @PathVariable String map) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return UNAUTHORIZED_RESULT;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(new BestBansEvaluation(gameDao)
                        .evaluateForMap(map).getResult(),
                        HttpStatus.OK)),
                "Best bans for map " + map + " have been requested."));
        return result;
    }
}
