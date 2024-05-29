package dev.luzifer;

import dev.luzifer.spring.PaladinsApplication;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@Slf4j
public class PaladinsWebservice {

  private static final String CONFIG_FILE_NAME = "application.properties";
  private static final Path CONFIG_FILE_PATH =
      Paths.get(System.getProperty("user.dir"), CONFIG_FILE_NAME);

  static {
    ensureFileExists(CONFIG_FILE_PATH, CONFIG_FILE_NAME);
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
    try (InputStream resourceStream =
        PaladinsWebservice.class.getClassLoader().getResourceAsStream(resourceName)) {
      if (resourceStream == null) {
        log.error("Resource {} not found", resourceName);
        return;
      }
      Files.copy(resourceStream, filePath, StandardCopyOption.REPLACE_EXISTING);
      log.info("Successfully copied {} to {}", resourceName, filePath);
    } catch (Exception exception) {
      log.error("Failed to copy {} file from resources", resourceName, exception);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(PaladinsApplication.class, args);
  }
}
