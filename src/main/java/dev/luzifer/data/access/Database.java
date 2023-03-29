package dev.luzifer.data.access;

import dev.luzifer.Main;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component("database")
public class Database {

    private static final File CREDENTIALS_FILE = new File("database.properties");

    static {

        if(!CREDENTIALS_FILE.exists()) {

            try {
                CREDENTIALS_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(InputStream input = Main.class.getClassLoader().getResourceAsStream("database.properties")) {

                InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader);

                try(FileWriter fileWriter = new FileWriter(CREDENTIALS_FILE)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null;) {
                        stringBuilder.append(line).append("\n");
                    }
                    fileWriter.write(stringBuilder.toString());
                }
            } catch(Exception ignored) {
            }
        }
    }

    private final String url;
    private final String username;
    private final byte[] password;

    private Connection connection;

    Database() {
        try {
            List<String> lines = Files.readAllLines(CREDENTIALS_FILE.toPath());

            this.url = lines.get(0).split("url:")[1];
            this.username = lines.get(1).split("username:")[1];
            this.password = lines.get(2).split("password:")[1].getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(GameDto[] games) {

        if(!isConnection())
            connect();

        String sql = "INSERT INTO games (id, map_name, ranked, average_rank, banned_champ1, banned_champ2, banned_champ3, banned_champ4, banned_champ5, banned_champ6, team1_points, team2_points, duration, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement champStatement = connection.prepareStatement(
                     "INSERT INTO champs (id, match_id, won, category_id, talent_id, deck_card1, deck_card2, deck_card3, deck_card4, deck_card5, deck_card1_level, deck_card2_level, deck_card3_level, deck_card4_level, deck_card5_level, item1, item2, item3, item4, item1Level, item2Level, item3Level, item4Level, kills, deaths, assists, damage_done, damage_taken, damage_shielded, heal, self_heal) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {

            try (PreparedStatement gamesStatement = connection.prepareStatement(sql)) {
                for(GameDto gameDto : games) {
                    gamesStatement.setInt(1, gameDto.getId());
                    gamesStatement.setString(2, gameDto.getMapName());
                    gamesStatement.setInt(3, gameDto.getRanked());
                    gamesStatement.setInt(4, gameDto.getAverageRank());
                    gamesStatement.setInt(5, gameDto.getBannedChamp1());
                    gamesStatement.setInt(6, gameDto.getBannedChamp2());
                    gamesStatement.setInt(7, gameDto.getBannedChamp3());
                    gamesStatement.setInt(8, gameDto.getBannedChamp4());
                    gamesStatement.setInt(9, gameDto.getBannedChamp5());
                    gamesStatement.setInt(10, gameDto.getBannedChamp6());
                    gamesStatement.setInt(11, gameDto.getTeam1Points());
                    gamesStatement.setInt(12, gameDto.getTeam2Points());
                    gamesStatement.setLong(13, gameDto.getDuration());
                    gamesStatement.setLong(14, gameDto.getTimestamp());
                    gamesStatement.addBatch();

                    for(ChampDto champ : gameDto.getChamps()) {
                        champStatement.setInt(1, champ.getId());
                        champStatement.setInt(2, gameDto.getId());
                        champStatement.setInt(3, champ.getWon());
                        champStatement.setInt(4, champ.getCategoryId());
                        champStatement.setInt(5, champ.getTalentId());
                        champStatement.setInt(6, champ.getDeckCard1());
                        champStatement.setInt(7, champ.getDeckCard2());
                        champStatement.setInt(8, champ.getDeckCard3());
                        champStatement.setInt(9, champ.getDeckCard4());
                        champStatement.setInt(10, champ.getDeckCard5());
                        champStatement.setInt(11, champ.getDeckCard1Level());
                        champStatement.setInt(12, champ.getDeckCard2Level());
                        champStatement.setInt(13, champ.getDeckCard3Level());
                        champStatement.setInt(14, champ.getDeckCard4Level());
                        champStatement.setInt(15, champ.getDeckCard5Level());
                        champStatement.setInt(16, champ.getItem1());
                        champStatement.setInt(17, champ.getItem2());
                        champStatement.setInt(18, champ.getItem3());
                        champStatement.setInt(19, champ.getItem4());
                        champStatement.setInt(20, champ.getItem1Level());
                        champStatement.setInt(21, champ.getItem2Level());
                        champStatement.setInt(22, champ.getItem3Level());
                        champStatement.setInt(23, champ.getItem4Level());
                        champStatement.setInt(24, champ.getKills());
                        champStatement.setInt(25, champ.getDeaths());
                        champStatement.setInt(26, champ.getAssists());
                        champStatement.setLong(27, champ.getDamageDone());
                        champStatement.setLong(28, champ.getDamageTaken());
                        champStatement.setLong(29, champ.getDamageShielded());
                        champStatement.setLong(30, champ.getHeal());
                        champStatement.setLong(31, champ.getSelfHeal());
                        champStatement.addBatch();
                    }
                }
                gamesStatement.executeBatch();
                champStatement.executeBatch();
                Main.LOGGER.info("INSERTED A BATCH WITH " + games.length + " (+x10 CHAMPS)ENTRIES INTO THE DATABASE");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            champStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(url, username, new String(password));
            ensureTableExists();
            Main.LOGGER.info("CONNECTED TO THE DATABASE");
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countEntries() {

        if(!isConnection())
            connect();

        String sql = "SELECT COUNT(*) FROM games";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public GameDto[] fetchAll() {

        if(!isConnection())
            connect();

        String sql = "SELECT * FROM games";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();
            List<GameDto> games = new ArrayList<>();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");

                ChampDto[] champs = fetchChamps(id);
                GameDto game = new GameDto(id,
                        resultSet.getString("map_name"),
                        resultSet.getInt("ranked"),
                        resultSet.getInt("average_rank"),
                        resultSet.getInt("banned_champ1"),
                        resultSet.getInt("banned_champ2"),
                        resultSet.getInt("banned_champ3"),
                        resultSet.getInt("banned_champ4"),
                        resultSet.getInt("banned_champ5"),
                        resultSet.getInt("banned_champ6"),
                        resultSet.getInt("team1_points"),
                        resultSet.getInt("team2_points"),
                        champs,
                        resultSet.getLong("duration"),
                        resultSet.getLong("timestamp"));
                games.add(game);
            }

            return games.toArray(new GameDto[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ChampDto[] fetchChamps(int matchId) {

        String sql = "SELECT * FROM champs WHERE match_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, matchId);

            ResultSet resultSet = statement.executeQuery();
            List<ChampDto> champs = new ArrayList<>();

            while (resultSet.next()) {

                ChampDto champ = new ChampDto(resultSet.getInt("id"),
                        resultSet.getInt("won"),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("talent_id"),
                        resultSet.getInt("deck_card1"),
                        resultSet.getInt("deck_card2"),
                        resultSet.getInt("deck_card3"),
                        resultSet.getInt("deck_card4"),
                        resultSet.getInt("deck_card5"),
                        resultSet.getInt("deck_card1_level"),
                        resultSet.getInt("deck_card2_level"),
                        resultSet.getInt("deck_card3_level"),
                        resultSet.getInt("deck_card4_level"),
                        resultSet.getInt("deck_card5_level"),
                        resultSet.getInt("item1"),
                        resultSet.getInt("item2"),
                        resultSet.getInt("item3"),
                        resultSet.getInt("item4"),
                        resultSet.getInt("item1_level"),
                        resultSet.getInt("item2_level"),
                        resultSet.getInt("item3_level"),
                        resultSet.getInt("item4_level"),
                        resultSet.getInt("kills"),
                        resultSet.getInt("deaths"),
                        resultSet.getInt("assists"),
                        resultSet.getLong("damage_done"),
                        resultSet.getLong("damage_taken"),
                        resultSet.getLong("damage_shielded"),
                        resultSet.getLong("heal"),
                        resultSet.getLong("self_heal"));
                champs.add(champ);
            }

            return champs.toArray(new ChampDto[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureTableExists() {

        String champTableSql = "CREATE TABLE IF NOT EXISTS champs (\n" +
                "  hurensohn INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "  id INTEGER NOT NULL DEFAULT 0,\n" +
                "  match_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  won INTEGER NOT NULL DEFAULT 0,\n" +
                "  category_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  talent_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card1 INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card2 INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card3 INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card4 INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card5 INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card1_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card2_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card3_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card4_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  deck_card5_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  item1 INTEGER NOT NULL DEFAULT 0,\n" +
                "  item2 INTEGER NOT NULL DEFAULT 0,\n" +
                "  item3 INTEGER NOT NULL DEFAULT 0,\n" +
                "  item4 INTEGER NOT NULL DEFAULT 0,\n" +
                "  item1Level INTEGER NOT NULL DEFAULT 0,\n" +
                "  item2Level INTEGER NOT NULL DEFAULT 0,\n" +
                "  item3Level INTEGER NOT NULL DEFAULT 0,\n" +
                "  item4Level INTEGER NOT NULL DEFAULT 0,\n" +
                "  kills INTEGER NOT NULL DEFAULT 0,\n" +
                "  deaths INTEGER NOT NULL DEFAULT 0,\n" +
                "  assists INTEGER NOT NULL DEFAULT 0,\n" +
                "  damage_done BIGINT NOT NULL DEFAULT 0,\n" +
                "  damage_taken BIGINT NOT NULL DEFAULT 0,\n" +
                "  damage_shielded BIGINT NOT NULL DEFAULT 0,\n" +
                "  heal BIGINT NOT NULL DEFAULT 0,\n" +
                "  self_heal BIGINT NOT NULL DEFAULT 0,\n" +
                "  PRIMARY KEY (hurensohn),\n" +
                "  FOREIGN KEY (match_id) REFERENCES games(id)\n" +
                ");";

        String gameTableSql = "CREATE TABLE IF NOT EXISTS games (\n" +
                "  id INTEGER NOT NULL DEFAULT 0,\n" +
                "  map_name VARCHAR(255) NOT NULL DEFAULT 'INVALID',\n" +
                "  ranked INTEGER NOT NULL DEFAULT 0,\n" +
                "  average_rank INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ1 INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ2 INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ3 INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ4 INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ5 INTEGER NOT NULL DEFAULT 0,\n" +
                "  banned_champ6 INTEGER NOT NULL DEFAULT 0,\n" +
                "  team1_points INTEGER NOT NULL DEFAULT 0,\n" +
                "  team2_points INTEGER NOT NULL DEFAULT 0,\n" +
                "  duration BIGINT NOT NULL DEFAULT 0,\n" +
                "  timestamp BIGINT NOT NULL DEFAULT 0,\n" +
                "  PRIMARY KEY (id)\n" +
                ");";

        if(!isConnection())
            connect();

        try(PreparedStatement statement = connection.prepareStatement(gameTableSql)) {
            statement.executeUpdate();
            Main.LOGGER.info("CREATED TABLE games");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = connection.prepareStatement(champTableSql)) {
            statement.executeUpdate();
            Main.LOGGER.info("CREATED TABLE champs");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
