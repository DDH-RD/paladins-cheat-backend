package dev.luzifer.spring;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.Database;
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

import java.util.concurrent.CompletableFuture;

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
        CompletableFuture.runAsync(() -> gameDao.initializeDatabase());
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startApplication() {
        Webservice.REST_LOGGER.info("STARTING APPLICATION");
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) {
        Webservice.REST_LOGGER.info("CLOSING APPLICATION: " + event.getApplicationContext().getDisplayName());
    }
}
