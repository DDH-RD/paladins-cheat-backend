package dev.luzifer;

import dev.luzifer.spring.PaladinsApplication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@Slf4j
public class PaladinsWebservice {

  private static final String CONFIG_FILE_NAME = "application.properties";
  private static final Path CONFIG_FILE_PATH =
      Paths.get(System.getProperty("user.dir"), CONFIG_FILE_NAME);

  private static final String API_KEY;

  static {
    ensureFileExists(CONFIG_FILE_PATH, CONFIG_FILE_NAME);

    API_KEY =
        loadApiKeyFromProperties(CONFIG_FILE_PATH)
            .orElseThrow(
                () -> {
                  log.error(
                      "API key is missing in application.properties file at {}", CONFIG_FILE_PATH);
                  log.error(
                      "Please add/change the API key in the file and restart the application");
                  return new IllegalStateException("API key is missing");
                });

    log.info("API key {} loaded successfully", API_KEY);
  }

  private static void ensureFileExists(Path filePath, String fileName) {
    if (!Files.exists(filePath)) {
      try {
        Files.createFile(filePath);
        fillFileFromResources(filePath, fileName);
        log.info("Created {} file at {}", fileName, filePath);
        log.info("Please restart the application with --spring.config.location={}", filePath);
      } catch (IOException e) {
        log.error("Failed to create {} file at {}", fileName, filePath, e);
      }
    }
  }

  private static void fillFileFromResources(Path filePath, String resourceName) {
    try {
      Path resourcePath =
          Path.of(PaladinsWebservice.class.getClassLoader().getResource(resourceName).toURI());
      Files.copy(resourcePath, filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception exception) {
      log.error("Failed to copy {} file from resources", resourceName, exception);
    }
  }

  private static Optional<String> loadApiKeyFromProperties(Path filePath) {
    Properties properties = new Properties();
    try {
      properties.load(Files.newInputStream(filePath));
    } catch (IOException e) {
      log.error("Failed to load {} file at {}", CONFIG_FILE_NAME, filePath, e);
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
