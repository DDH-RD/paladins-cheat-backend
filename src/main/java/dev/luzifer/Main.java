package dev.luzifer;

import dev.luzifer.spring.Application;
import org.springframework.boot.SpringApplication;

import java.util.logging.Logger;

public class Main {

    public static final Logger REST_LOGGER = Logger.getLogger("Paladins-REST");
    public static final Logger DATABASE_LOGGER = Logger.getLogger("Paladins-Database");

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
