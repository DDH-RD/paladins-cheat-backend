package dev.luzifer.spring;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.GameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@PropertySource("classpath:application.properties")
public class Application {

    @Value("${spring.mvc.async.request-timeout}")
    private String async_timeout;

    @Value("${server.servlet.session.timeout}")
    private String session_timeout;

    @Value("${server.connection-timeout}")
    private String connection_timeout;

    @Autowired
    private GameDao gameDao;

    @EventListener(ApplicationReadyEvent.class)
    public void connectToDatabase() {
        shootFormattedDebugMessage(1, 2, 3, 4);
        gameDao.initializeDatabase();
    }

    private void shootFormattedDebugMessage(int duplicated, int success, int error, int notFound) {

        Webservice.REST_LOGGER.info(success + " game(s) has been saved to the database.");

        if (notFound > 0) {
            Webservice.REST_LOGGER.warning(">     | " + notFound + " game(s) were not inserted");
        }

        if (error > 0) {
            Webservice.REST_LOGGER.warning(">             | " + error + " game(s) had errors");
        }

        if (duplicated > 0) {
            Webservice.REST_LOGGER.warning(">             | " + duplicated + " game(s) are duplicates");
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startApplication() {
        Webservice.REST_LOGGER.info("Booting up application...");
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) {
        Webservice.REST_LOGGER.info("Closing application: " + event.getApplicationContext().getDisplayName());
        Webservice.REST_LOGGER.info("oh no i died :(");
    }
}
