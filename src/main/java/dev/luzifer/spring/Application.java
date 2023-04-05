package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.data.access.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class Application {

    @Autowired
    private Database database;

    @EventListener(ApplicationReadyEvent.class)
    public void connectToDatabase() {
        database.connect();
        Main.LOGGER.info("CONNECTED TO DATABASE");
    }
}
