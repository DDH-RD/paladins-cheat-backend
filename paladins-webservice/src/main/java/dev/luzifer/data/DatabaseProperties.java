package dev.luzifer.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseProperties {

  private String url;
  private String username;
  private String password;
  private String driverClassName;
}
