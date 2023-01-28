package dev.luzifer;

import dev.luzifer.spring.Application;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.util.logging.Logger;

public class Main {

    public static final Logger LOGGER = Logger.getLogger("Paladins");
    public static final File JAR_POSITION = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
