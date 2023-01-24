package dev.luzifer.spring;

import dev.luzifer.data.Mapper;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.MatchMapper;
import dev.luzifer.data.match.info.GameInfo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
public class Application {
    
    public static final Mapper<MatchId, GameInfo> MATCH_ID_GAME_INFO_MAPPER = new MatchMapper();
    
}
