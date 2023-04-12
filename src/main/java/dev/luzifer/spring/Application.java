package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.data.access.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.sql.SQLException;

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
    private Database database;

    @EventListener(ApplicationReadyEvent.class)
    public void connectToDatabase() {
        database.initialize();
        Main.DATABASE_LOGGER.info("CONNECTED TO DATABASE");
    }

    @EventListener(ApplicationStartedEvent.class)
    public void startApplication() {
        Main.REST_LOGGER.info("STARTING APPLICATION");
        Main.REST_LOGGER.info("ASYNC TIMEOUT: " + async_timeout);
        Main.REST_LOGGER.info("SESSION TIMEOUT: " + session_timeout);
        Main.REST_LOGGER.info("CONNECTION TIMEOUT: " + connection_timeout);
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) throws SQLException {
        Main.REST_LOGGER.info("CLOSING APPLICATION: " + event.getApplicationContext().getDisplayName());
        Main.DATABASE_LOGGER.info("CLOSING DATABASE CONNECTION AND CLEARING CACHE");
        Database.getConnectionPool().closeAllConnections();
        Database.getRecordCache().clear();
    }
}
