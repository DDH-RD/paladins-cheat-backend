package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.DatabaseResult;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.spring.ApplicationAccessPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.logging.Level;

@RestController
@RequestMapping(ApplicationAccessPoint.GAME)
public class GameController extends AbstractController {

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
                int error = 0;
                int notFound = 0;
                for (GameDto gameDto : gameDtos) {
                    DatabaseResult<Void> result = gameDao.saveGameData(gameDto);
                    switch (result.getDatabaseResultType()) {
                        case SUCCESS -> {}
                        case DUPLICATE -> duplicated++;
                        case ERROR -> error++;
                        case NOT_FOUND -> notFound++;
                        default ->
                                throw new IllegalStateException("Unknown DatabaseResultType: " + result.getDatabaseResultType());
                    }

                    if(result.getDatabaseResultType() == DatabaseResult.DatabaseResultType.DUPLICATE) {
                        Webservice.DATABASE_LOGGER.warning(result.getMessage());
                    }
                }
                shootFormattedDebugMessage(duplicated, error, notFound);
            }, "Post games request with initially " + gameDtos.length + " games has been processed.");
        });
    }

    @GetMapping(ApplicationAccessPoint.GET_LATEST_MATCH_ID)
    public DeferredResult<ResponseEntity<?>> getLatestMatchId(@PathVariable String apiKey) {
        if (couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.log(Level.WARNING, "I've registered an unauthorized access attempt.");
            return UNAUTHORIZED_RESULT;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(gameDao.getLatestMatchId(), HttpStatus.OK)),
                "Latest match id has been requested."));
        return result;
    }

    private void shootFormattedDebugMessage(int duplicated, int error, int notFound) {
        if(isZero(duplicated) && isZero(error) && isZero(notFound)) {
            return;
        }

        Webservice.REST_LOGGER.info("Failed to save " + (duplicated + error + notFound) + " game(s).");
        if (notFound > 0) {
            Webservice.REST_LOGGER.warning("  | " + notFound + " game(s) were not found(?)");
        }

        if (error > 0) {
            Webservice.REST_LOGGER.warning("  | " + error + " game(s) had errors");
        }

        if (duplicated > 0) {
            Webservice.REST_LOGGER.warning("  | " + duplicated + " game(s) are duplicates");
        }
    }

    private boolean isZero(int number) {
        return number == 0;
    }
}
