package dev.luzifer.spring;

import dev.luzifer.Main;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class PingController {

    @GetMapping("ping")
    public ResponseEntity<String> ping() {
        Main.LOGGER.info("PING RECEIVED");
        return ResponseEntity.ok("pong");
    }
}
