package dev.luzifer.spring.controller;

import dev.luzifer.data.dto.ChampDto;
import dev.luzifer.data.dto.GameDto;
import dev.luzifer.data.service.ChampService;
import dev.luzifer.data.service.MatchService;
import dev.luzifer.data.service.PlayerService;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${api.match}")
public class MatchController {

  private final ChampService champService;
  private final MatchService matchService;
  private final PlayerService playerService;

  @Autowired
  public MatchController(
      ChampService champService, MatchService matchService, PlayerService playerService) {
    this.champService = champService;
    this.matchService = matchService;
    this.playerService = playerService;
  }

  @Async("taskExecutor")
  @PostMapping
  public CompletableFuture<ResponseEntity<?>> pushMatches(@RequestBody GameDto[] gameDtos) {
    try {
      if (gameDtos == null || gameDtos.length == 0) {
        return CompletableFuture.completedFuture(
            ResponseEntity.badRequest().body("No game information provided."));
      }

      ChampDto[] champDtos = pullChamps(gameDtos);
      matchService.processMatchData(gameDtos);
      champService.processChamps(champDtos);
      playerService.processPlayers(champDtos);

      log.info("Saved {} games with {} champ information", gameDtos.length, champDtos.length);
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    } catch (Exception e) {
      log.error("Error processing game information", e);
      return CompletableFuture.completedFuture(
          ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Error processing game information."));
    }
  }

  @Async("taskExecutor")
  @GetMapping("${api.match.count}")
  public CompletableFuture<ResponseEntity<Long>> countGameInfos() {
    long count = matchService.countMatches();
    return CompletableFuture.completedFuture(ResponseEntity.ok(count));
  }

  private ChampDto[] pullChamps(GameDto[] gameDtoArray) {
    return Arrays.stream(gameDtoArray)
        .flatMap(gameDto -> Arrays.stream(gameDto.getChamps()))
        .toArray(ChampDto[]::new);
  }
}
