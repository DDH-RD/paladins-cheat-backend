package dev.luzifer.data.access;

import dev.luzifer.data.dto.GameDto;
import org.springframework.stereotype.Component;

@Component("gameDao")
public class GameDao {

    private final Database database = new Database();

    public void initializeDatabase() {
        database.initialize();
    }

    public void saveGameData(GameDto gameDto) {
    }
}
