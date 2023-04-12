package dev.luzifer;

import dev.luzifer.spring.Application;
import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;

import java.util.logging.Logger;

@UtilityClass
public class Main {

    public static final Logger REST_LOGGER = Logger.getLogger("Paladins-REST");
    public static final Logger DATABASE_LOGGER = Logger.getLogger("Paladins-Database");

    private static final Credentials CREDENTIALS = Credentials.get();

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    public static String getApiKey() {
        return CREDENTIALS.getApiKey();
    }

    public static String getDatabaseUrl() {
        return CREDENTIALS.getDatabaseUrl();
    }

    public static String getDatabaseUsername() {
        return CREDENTIALS.getDatabaseUsername();
    }

    public static char[] getDatabasePassword() {
        return CREDENTIALS.getDatabasePassword();
    }
}
