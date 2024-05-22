package dev.luzifer.spring;

import dev.luzifer.data.DatabaseProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseProperties.class)
@EnableJpaRepositories(basePackages = {"dev.luzifer.data"})
@ComponentScan(basePackages = {"dev.luzifer.data"})
@EntityScan(basePackages = {"dev.luzifer.database.objects"})
public class PaladinsApplication {}
