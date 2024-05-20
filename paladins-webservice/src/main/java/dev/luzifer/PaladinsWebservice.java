package dev.luzifer;

import dev.luzifer.spring.PaladinsApplication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@Slf4j
public class PaladinsWebservice {

  private static final String CONFIG_FILE_NAME = "application.properties";
  private static final String CREDENTIALS_FILE_NAME = "webservice.properties";
  private static final Path CREDENTIALS_FILE_PATH =
      Paths.get(System.getProperty("user.dir"), CREDENTIALS_FILE_NAME);
  private static final Path CONFIG_FILE_PATH =
      Paths.get(System.getProperty("user.dir"), CONFIG_FILE_NAME);

  private static final String API_KEY;

  static {
    ensureFileExists(CONFIG_FILE_PATH, CONFIG_FILE_NAME);
    ensureFileExists(CREDENTIALS_FILE_PATH, CREDENTIALS_FILE_NAME);

    API_KEY =
        loadApiKeyFromProperties(CREDENTIALS_FILE_PATH)
            .orElseThrow(
                () -> {
                  log.error(
                      "API key is missing in webservice.properties file at {}",
                      CREDENTIALS_FILE_PATH);
                  log.error("Please add your API key to the file and restart the application");
                  return new IllegalStateException("API key is missing");
                });
  }

  private static void ensureFileExists(Path filePath, String fileName) {
    if (!Files.exists(filePath)) {
      try {
        Files.createFile(filePath);
        log.info("Created {} file at {}", fileName, filePath);
      } catch (IOException e) {
        log.error("Failed to create {} file at {}", fileName, filePath, e);
      }
    }
  }

  private static Optional<String> loadApiKeyFromProperties(Path filePath) {
    Properties properties = new Properties();
    try {
      properties.load(Files.newInputStream(filePath));
    } catch (IOException e) {
      log.error("Failed to load {} file at {}", CREDENTIALS_FILE_NAME, filePath, e);
    }
    return Optional.ofNullable(properties.getProperty("api.key"));
  }

  public static void main(String[] args) {
    SpringApplication.run(PaladinsApplication.class, args);
  }

  public static String getApiKey() {
    return API_KEY;
  }
}
