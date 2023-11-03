package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.spring.ApplicationAccessPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    
    @GetMapping(ApplicationAccessPoint.API_KEY_REQUEST_PASSWORD)
    public ResponseEntity<String> getApiKeyRequestPassword(@PathVariable String password) {
        if(!Webservice.getApiKeyPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Wrong password!");
        }
        
        return ResponseEntity.ok(Webservice.getApiKey());
    }
}
