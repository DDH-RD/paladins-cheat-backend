package dev.luzifer.spring;

import dev.luzifer.data.access.GameDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"dev.luzifer.data.access"})
public class ApplicationConfiguration {

    @Bean
    public GameDao gameDao() {
        return new GameDao();
    }
}
