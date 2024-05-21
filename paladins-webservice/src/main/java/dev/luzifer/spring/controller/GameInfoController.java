package dev.luzifer.spring.controller;

import dev.luzifer.database.Database;
import dev.luzifer.database.dto.GameDto;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameinfo")
public class GameInfoController {

  private final Database database;

  @Autowired
  public GameInfoController(Database database) {
    this.database = database;
  }

  @Async("taskExecutor")
  @PostMapping
  public CompletableFuture<ResponseEntity<Void>> pushGameInfo(@RequestBody GameDto[] gameDtos) {
    database.saveGames(gameDtos);
    return CompletableFuture.completedFuture(ResponseEntity.ok().build());
  }

  @Async("taskExecutor")
  @GetMapping("/count")
  public CompletableFuture<ResponseEntity<Long>> countGameInfos() {
    long count = database.countGameInfos();
    return CompletableFuture.completedFuture(ResponseEntity.ok(count));
  }
}