package dev.luzifer;

import dev.luzifer.spring.Application;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {

    public static final Logger LOGGER = Logger.getLogger("Paladins");

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class);
    }

}
