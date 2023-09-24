package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class PingController {

    @GetMapping("ping")
    public ResponseEntity<String> ping() {
        Webservice.REST_LOGGER.info("Received ping request.");
        return ResponseEntity.ok("pong");
    }
}
