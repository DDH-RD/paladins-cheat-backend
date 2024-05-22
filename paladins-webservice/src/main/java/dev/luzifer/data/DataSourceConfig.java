package dev.luzifer.data;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

  @Autowired private DatabaseProperties databaseProperties;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(databaseProperties.getDriverClassName());
    dataSource.setUrl(databaseProperties.getUrl());
    dataSource.setUsername(databaseProperties.getUsername());
    dataSource.setPassword(databaseProperties.getPassword());
    return dataSource;
  }
}
