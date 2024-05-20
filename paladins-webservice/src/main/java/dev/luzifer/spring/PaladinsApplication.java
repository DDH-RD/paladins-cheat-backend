package dev.luzifer.spring;

import dev.luzifer.database.DatabaseProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseProperties.class)
@ComponentScan({"dev.luzifer.spring", "dev.luzifer.database"})
public class PaladinsApplication {}
