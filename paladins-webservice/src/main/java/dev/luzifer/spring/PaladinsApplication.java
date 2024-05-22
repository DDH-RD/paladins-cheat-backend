package dev.luzifer.spring;

import dev.luzifer.data.DatabaseProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(DatabaseProperties.class)
@EnableJpaRepositories(basePackages = {"dev.luzifer.data.repository"})
@ComponentScan(basePackages = {"dev.luzifer.data"})
@EntityScan(basePackages = {"dev.luzifer.data.entity"})
public class PaladinsApplication {}
