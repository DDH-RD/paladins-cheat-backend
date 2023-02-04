package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.data.access.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
public class Application {

    @Autowired
    private GameDao gameDao;

    @EventListener(ApplicationStartedEvent.class)
    public void fillCacheFromDatabase() {
        Main.LOGGER.info("FETCHED ALL DATA FROM DB");
    }
    
}
