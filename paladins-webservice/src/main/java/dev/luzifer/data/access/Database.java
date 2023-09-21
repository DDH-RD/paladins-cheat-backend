package dev.luzifer.data.access;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.shit.ChampInfo;
import dev.luzifer.data.access.shit.GameInfo;
import dev.luzifer.data.access.shit.PlayerInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;

public class Database {

    private Connection connection;

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                Webservice.getDatabaseUrl(),
                Webservice.getDatabaseUsername(),
                String.copyValueOf(Webservice.getDatabasePassword()));
    }

    public void initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Webservice.DATABASE_LOGGER.info("Loaded JDBC Driver");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        connect();
        createMapInfoTable();
        createChampInfoTable();
        createGameInfoTable();
        createPlayerInfoTable();

        Webservice.DATABASE_LOGGER.info("Database initialized");
    }

    protected boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    protected void connect() {
        try {
            int threads = Runtime.getRuntime().availableProcessors();
            connection = createConnection();
            connection.setNetworkTimeout(Executors.newFixedThreadPool(threads), 60 * 1000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMapInfo(String mapName) {
        if (!isConnected()) {
            connect();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO MapInfo (mapName) VALUES (?)")) {
            preparedStatement.setString(1, mapName);
            preparedStatement.executeUpdate();
            Webservice.DATABASE_LOGGER.info("MapInfo record inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting MapInfo record", e);
        }
    }

    public void insertChampInfo(ChampInfo champInfo) {
        if (!isConnected()) {
            connect();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO ChampInfo (champId, leagueTier, leaguePoints, champLevel, " +
                        "won, categoryId, goldEarned, talentId, deckCard1, deckCard2, deckCard3, " +
                        "deckCard4, deckCard5, deckCard1Level, deckCard2Level, deckCard3Level, " +
                        "deckCard4Level, deckCard5Level, item1, item2, item3, item4, item1Level, " +
                        "item2Level, item3Level, item4Level, killingSpree, kills, deaths, assists, " +
                        "damageDone, damageTaken, damageShielded, heal, selfHeal, matchId, playerId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, champInfo.getChampId());
            preparedStatement.setInt(2, champInfo.getLeagueTier());
            preparedStatement.setInt(3, champInfo.getLeaguePoints());
            preparedStatement.setInt(4, champInfo.getChampLevel());
            preparedStatement.setInt(5, champInfo.getWon());
            preparedStatement.setInt(6, champInfo.getCategoryId());
            preparedStatement.setInt(7, champInfo.getGoldEarned());
            preparedStatement.setInt(8, champInfo.getTalentId());
            preparedStatement.setInt(9, champInfo.getDeckCard1());
            preparedStatement.setInt(10, champInfo.getDeckCard2());
            preparedStatement.setInt(11, champInfo.getDeckCard3());
            preparedStatement.setInt(12, champInfo.getDeckCard4());
            preparedStatement.setInt(13, champInfo.getDeckCard5());
            preparedStatement.setInt(14, champInfo.getDeckCard1Level());
            preparedStatement.setInt(15, champInfo.getDeckCard2Level());
            preparedStatement.setInt(16, champInfo.getDeckCard3Level());
            preparedStatement.setInt(17, champInfo.getDeckCard4Level());
            preparedStatement.setInt(18, champInfo.getDeckCard5Level());
            preparedStatement.setInt(19, champInfo.getItem1());
            preparedStatement.setInt(20, champInfo.getItem2());
            preparedStatement.setInt(21, champInfo.getItem3());
            preparedStatement.setInt(22, champInfo.getItem4());
            preparedStatement.setInt(23, champInfo.getItem1Level());
            preparedStatement.setInt(24, champInfo.getItem2Level());
            preparedStatement.setInt(25, champInfo.getItem3Level());
            preparedStatement.setInt(26, champInfo.getItem4Level());
            preparedStatement.setInt(27, champInfo.getKillingSpree());
            preparedStatement.setInt(28, champInfo.getKills());
            preparedStatement.setInt(29, champInfo.getDeaths());
            preparedStatement.setInt(30, champInfo.getAssists());
            preparedStatement.setInt(31, champInfo.getDamageDone());
            preparedStatement.setInt(32, champInfo.getDamageTaken());
            preparedStatement.setInt(33, champInfo.getDamageShielded());
            preparedStatement.setInt(34, champInfo.getHeal());
            preparedStatement.setInt(35, champInfo.getSelfHeal());
            preparedStatement.setInt(36, champInfo.getMatchId());
            preparedStatement.setInt(37, champInfo.getPlayerId());

            preparedStatement.executeUpdate();
            Webservice.DATABASE_LOGGER.info("ChampInfo record inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting ChampInfo record", e);
        }
    }

    public void insertGameInfo(GameInfo gameInfo) {
        if (!isConnected()) {
            connect();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO GameInfo (matchId, ranked, averageRank, mapId, bannedChamp1, " +
                        "bannedChamp2, bannedChamp3, bannedChamp4, bannedChamp5, bannedChamp6, " +
                        "bannedChamp7, bannedChamp8, team1Points, team2Points, duration, timestamp, " +
                        "season) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, gameInfo.getMatchId());
            preparedStatement.setInt(2, gameInfo.getRanked());
            preparedStatement.setInt(3, gameInfo.getAverageRank());
            preparedStatement.setInt(4, gameInfo.getMapId());
            preparedStatement.setInt(5, gameInfo.getBannedChamp1());
            preparedStatement.setInt(6, gameInfo.getBannedChamp2());
            preparedStatement.setInt(7, gameInfo.getBannedChamp3());
            preparedStatement.setInt(8, gameInfo.getBannedChamp4());
            preparedStatement.setInt(9, gameInfo.getBannedChamp5());
            preparedStatement.setInt(10, gameInfo.getBannedChamp6());
            preparedStatement.setInt(11, gameInfo.getBannedChamp7());
            preparedStatement.setInt(12, gameInfo.getBannedChamp8());
            preparedStatement.setInt(13, gameInfo.getTeam1Points());
            preparedStatement.setInt(14, gameInfo.getTeam2Points());
            preparedStatement.setLong(15, gameInfo.getDuration());
            preparedStatement.setLong(16, gameInfo.getTimestamp());
            preparedStatement.setDouble(17, gameInfo.getSeason());

            preparedStatement.executeUpdate();
            Webservice.DATABASE_LOGGER.info("GameInfo record inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting GameInfo record", e);
        }
    }

    public void insertPlayerInfo(PlayerInfo playerInfo) {
        if (!isConnected()) {
            connect();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO PlayerInfo (playerId, playerName, region, platformId) " +
                        "VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, playerInfo.getPlayerId());
            preparedStatement.setString(2, playerInfo.getPlayerName());
            preparedStatement.setString(3, playerInfo.getRegion());
            preparedStatement.setInt(4, playerInfo.getPlatformId());

            preparedStatement.executeUpdate();
            Webservice.DATABASE_LOGGER.info("PlayerInfo record inserted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting PlayerInfo record", e);
        }
    }

    public void createMapInfoTable() {
        createTableIfNotExists("MapInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "mapName VARCHAR(255)"
        );
    }

    public void createChampInfoTable() {
        createTableIfNotExists("ChampInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "champId INT,"
                        + "leagueTier INT,"
                        + "leaguePoints INT,"
                        + "champLevel INT,"
                        + "won INT,"
                        + "categoryId INT,"
                        + "goldEarned INT,"
                        + "talentId INT,"
                        + "deckCard1 INT,"
                        + "deckCard2 INT,"
                        + "deckCard3 INT,"
                        + "deckCard4 INT,"
                        + "deckCard5 INT,"
                        + "deckCard1Level INT,"
                        + "deckCard2Level INT,"
                        + "deckCard3Level INT,"
                        + "deckCard4Level INT,"
                        + "deckCard5Level INT,"
                        + "item1 INT,"
                        + "item2 INT,"
                        + "item3 INT,"
                        + "item4 INT,"
                        + "item1Level INT,"
                        + "item2Level INT,"
                        + "item3Level INT,"
                        + "item4Level INT,"
                        + "killingSpree INT,"
                        + "kills INT,"
                        + "deaths INT,"
                        + "assists INT,"
                        + "damageDone INT,"
                        + "damageTaken INT,"
                        + "damageShielded INT,"
                        + "heal INT,"
                        + "selfHeal INT,"
                        + "matchId INT,"
                        + "playerId INT,"
                        + "FOREIGN KEY (matchId) REFERENCES GameInfo(matchId),"
                        + "FOREIGN KEY (playerId) REFERENCES PlayerInfo(playerId)"
        );
    }

    public void createGameInfoTable() {
        createTableIfNotExists("GameInfo",
                "matchId INT PRIMARY KEY,"
                        + "ranked INT,"
                        + "averageRank INT,"
                        + "mapId INT,"
                        + "bannedChamp1 INT,"
                        + "bannedChamp2 INT,"
                        + "bannedChamp3 INT,"
                        + "bannedChamp4 INT,"
                        + "bannedChamp5 INT,"
                        + "bannedChamp6 INT,"
                        + "bannedChamp7 INT,"
                        + "bannedChamp8 INT,"
                        + "team1Points INT,"
                        + "team2Points INT,"
                        + "duration BIGINT,"
                        + "timestamp BIGINT,"
                        + "season DOUBLE"
        );
    }

    public void createPlayerInfoTable() {
        createTableIfNotExists("PlayerInfo",
                "playerId INT PRIMARY KEY,"
                        + "playerName VARCHAR(255),"
                        + "region VARCHAR(255),"
                        + "platformId INT"
        );
    }

    private void createTableIfNotExists(String tableName, String tableDefinition) {
        if (!isConnected()) {
            connect();
        }

        try (Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableDefinition + ")";
            statement.executeUpdate(createTableSQL);
            Webservice.DATABASE_LOGGER.info(tableName + " table created if not exists.");
        } catch (SQLException e) {
            throw new RuntimeException("Error creating " + tableName + " table", e);
        }
    }
}
