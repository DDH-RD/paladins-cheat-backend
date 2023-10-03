package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.spring.ApplicationAccessPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.function.Supplier;
import java.util.logging.Level;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(ApplicationAccessPoint.GET_COUNT)
public class CountController extends AbstractController {

    @GetMapping
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
}
