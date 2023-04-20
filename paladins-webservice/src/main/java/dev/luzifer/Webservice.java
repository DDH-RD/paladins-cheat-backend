package dev.luzifer;

import dev.luzifer.spring.Application;
import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

@UtilityClass
public class Webservice {

    public static final Logger REST_LOGGER = Logger.getLogger("Paladins-REST");
    public static final Logger DATABASE_LOGGER = Logger.getLogger("Paladins-Database");

    private static final File CREDENTIALS_FILE = new File("webservice.properties");

    private static String API_KEY;
    private static String DATABASE_URL;
    private static String DATABASE_USERNAME;
    private static char[] DATABASE_PASSWORD;

    static {

        if(!CREDENTIALS_FILE.exists()) {

            try {
                CREDENTIALS_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(InputStream input = Webservice.class.getClassLoader().getResourceAsStream("webservice.properties")) {

                InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);

                try(FileWriter fileWriter = new FileWriter(CREDENTIALS_FILE)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null;) {
                        stringBuilder.append(line).append("\n");
                    }
                    fileWriter.write(stringBuilder.toString());
                }
            } catch(Exception ignored) {
            }
        }

        try {
            List<String> lines = Files.readAllLines(CREDENTIALS_FILE.toPath());
            lines.forEach(line -> {
                if(line.startsWith("api-key:")) API_KEY = line.substring(8);
                if(line.startsWith("database-url:")) DATABASE_URL = line.substring(13);
                if(line.startsWith("database-username:")) DATABASE_USERNAME = line.substring(18);
                if(line.startsWith("database-password:")) DATABASE_PASSWORD = line.substring(18).toCharArray();
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }

    public static String getDatabaseUsername() {
        return DATABASE_USERNAME;
    }

    public static char[] getDatabasePassword() {
        return DATABASE_PASSWORD;
    }
}