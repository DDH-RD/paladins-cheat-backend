package dev.luzifer.data.access;

import dev.luzifer.Main;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.spring.controller.GameController;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component("database")
public class Database {

    private Connection connection;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.connection = DriverManager.getConnection(
                    Main.getDatabaseUrl(),
                    Main.getDatabaseUsername(),
                    String.copyValueOf(Main.getDatabasePassword()));
            ensureTableExists();
            Main.DATABASE_LOGGER.info("CONNECTED TO THE DATABASE");
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertChampData(ChampData[] champData) {
        if(!isConnected())
            connect();

        ensureTableExists();
        String sql = "INSERT IGNORE INTO champdata (champ_id, match_id, map_name, ranked, average_rank, banned_champ1, banned_champ2, banned_champ3, banned_champ4, banned_champ5, banned_champ6, team1_points, team2_points, duration, timestamp, season, player_id, player_name, region, platform_id, league_tier, league_points, champ_level, won, category_id, gold_earned, talent_id, deck_card1, deck_card2, deck_card3, deck_card4, deck_card5, deck_card1_level, deck_card2_level, deck_card3_level, deck_card4_level, deck_card5_level, item1, item2, item3, item4, item1Level, item2Level, item3Level, item4Level, killing_spree, kills, deaths, assists, damage_done, damage_taken, damage_shielded, heal, self_heal)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (ChampData data : champData) {
                statement.setInt(1, data.getChampId());
                statement.setLong(2, data.getMatchId());
                statement.setString(3, data.getMapName());
                statement.setInt(4, data.getRanked());
                statement.setInt(5, data.getAverageRank());
                statement.setInt(6, data.getBannedChamp1());
                statement.setInt(7, data.getBannedChamp2());
                statement.setInt(8, data.getBannedChamp3());
                statement.setInt(9, data.getBannedChamp4());
                statement.setInt(10, data.getBannedChamp5());
                statement.setInt(11, data.getBannedChamp6());
                statement.setInt(12, data.getTeam1Points());
                statement.setInt(13, data.getTeam2Points());
                statement.setLong(14, data.getDuration());
                statement.setLong(15, data.getTimestamp());
                statement.setDouble(16, data.getSeason());
                statement.setInt(17, data.getPlayerId());
                statement.setString(18, data.getPlayerName());
                statement.setString(19, data.getRegion());
                statement.setInt(20, data.getPlatformId());
                statement.setInt(21, data.getLeagueTier());
                statement.setInt(22, data.getLeaguePoints());
                statement.setInt(23, data.getChampLevel());
                statement.setInt(24, data.getWon());
                statement.setInt(25, data.getCategoryId());
                statement.setInt(26, data.getGoldEarned());
                statement.setInt(27, data.getTalentId());
                statement.setInt(28, data.getDeckCard1());
                statement.setInt(29, data.getDeckCard2());
                statement.setInt(30, data.getDeckCard3());
                statement.setInt(31, data.getDeckCard4());
                statement.setInt(32, data.getDeckCard5());
                statement.setInt(33, data.getDeckCard1Level());
                statement.setInt(34, data.getDeckCard2Level());
                statement.setInt(35, data.getDeckCard3Level());
                statement.setInt(36, data.getDeckCard4Level());
                statement.setInt(37, data.getDeckCard5Level());
                statement.setInt(38, data.getItem1());
                statement.setInt(39, data.getItem2());
                statement.setInt(40, data.getItem3());
                statement.setInt(41, data.getItem4());
                statement.setInt(42, data.getItem1Level());
                statement.setInt(43, data.getItem2Level());
                statement.setInt(44, data.getItem3Level());
                statement.setInt(45, data.getItem4Level());
                statement.setInt(46, data.getKillingSpree());
                statement.setInt(47, data.getKills());
                statement.setInt(48, data.getDeaths());
                statement.setInt(49, data.getAssists());
                statement.setLong(50, data.getDamageDone());
                statement.setLong(51, data.getDamageTaken());
                statement.setLong(52, data.getDamageShielded());
                statement.setLong(53, data.getHeal());
                statement.setLong(54, data.getSelfHeal());
                statement.addBatch();
            }
            statement.executeBatch();
            Main.DATABASE_LOGGER.info("INSERTED " + champData.length + " ENTRIES INTO THE DATABASE");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert champ data", e);
        }
    }

    public int countEntries(GameController.MatchType matchType) {

        if(!isConnected())
            connect();

        String sql = "SELECT COUNT(*) FROM champdata";
        switch (matchType) {
            case RANKED:
                sql += " WHERE ranked = 1";
                break;
            case CASUAL:
                sql += " WHERE ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Main.DATABASE_LOGGER.info("COUNT " + resultSet.getInt(1) + " ENTRIES IN THE DATABASE");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public List<ChampData> fetchAllChampData(GameController.MatchType matchType) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata";
        switch (matchType) {
            case RANKED -> sql += " WHERE ranked = 1";
            case CASUAL -> sql += " WHERE ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForMatch(GameController.MatchType matchType, int matchId) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE match_id = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, matchId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForMatchOfCategory(GameController.MatchType matchType, int matchId, int categoryId) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE match_id = ? AND category_id = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, matchId);
            preparedStatement.setInt(2, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForChamp(GameController.MatchType matchType, int champId) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE champ_id = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, champId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForMap(GameController.MatchType matchType, String mapName) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE map_name = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mapName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForCategory(GameController.MatchType matchType, int categoryId) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE category_id = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public List<ChampData> fetchChampDataForMapOfCategory(GameController.MatchType matchType, String mapName, int categoryId) {

        List<ChampData> data = new ArrayList<>();

        String sql = "SELECT * FROM champdata WHERE map_name = ? AND category_id = ?";
        switch (matchType) {
            case RANKED -> sql += " AND ranked = 1";
            case CASUAL -> sql += " AND ranked = 0";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setInt(2, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(constructChampDataFromResultSet(resultSet));
                Main.DATABASE_LOGGER.info("FETCHED " + data.size() + " ENTRIES FROM THE DATABASE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    private ChampData constructChampDataFromResultSet(ResultSet resultSet) throws SQLException {
        return new ChampData(
                resultSet.getInt("match_id"),
                resultSet.getString("map_name"),
                resultSet.getInt("ranked"),
                resultSet.getInt("average_rank"),
                resultSet.getInt("banned_champ_1"),
                resultSet.getInt("banned_champ_2"),
                resultSet.getInt("banned_champ_3"),
                resultSet.getInt("banned_champ_4"),
                resultSet.getInt("banned_champ_5"),
                resultSet.getInt("banned_champ_6"),
                resultSet.getInt("team_1_points"),
                resultSet.getInt("team_2_points"),
                resultSet.getLong("duration"),
                resultSet.getLong("timestamp"),
                resultSet.getDouble("season"),
                resultSet.getInt("champ_id"),
                resultSet.getInt("player_id"),
                resultSet.getString("player_name"),
                resultSet.getString("region"),
                resultSet.getInt("platform_id"),
                resultSet.getInt("league_tier"),
                resultSet.getInt("league_points"),
                resultSet.getInt("champ_level"),
                resultSet.getInt("won"),
                resultSet.getInt("category_id"),
                resultSet.getInt("gold_earned"),
                resultSet.getInt("talent_id"),
                resultSet.getInt("deck_card_1"),
                resultSet.getInt("deck_card_2"),
                resultSet.getInt("deck_card_3"),
                resultSet.getInt("deck_card_4"),
                resultSet.getInt("deck_card_5"),
                resultSet.getInt("deck_card_1_level"),
                resultSet.getInt("deck_card_2_level"),
                resultSet.getInt("deck_card_3_level"),
                resultSet.getInt("deck_card_4_level"),
                resultSet.getInt("deck_card_5_level"),
                resultSet.getInt("item_1"),
                resultSet.getInt("item_2"),
                resultSet.getInt("item_3"),
                resultSet.getInt("item_4"),
                resultSet.getInt("item_1_level"),
                resultSet.getInt("item_2_level"),
                resultSet.getInt("item_3_level"),
                resultSet.getInt("item_4_level"),
                resultSet.getInt("killing_spree"),
                resultSet.getInt("kills"),
                resultSet.getInt("deaths"),
                resultSet.getInt("assists"),
                resultSet.getLong("damage_done"),
                resultSet.getLong("damage_taken"),
                resultSet.getLong("damage_shielded"),
                resultSet.getLong("heal"),
                resultSet.getLong("self_heal")
        );
    }

    private void ensureTableExists() {

        String champDataTableSql = "CREATE TABLE IF NOT EXISTS champdata (\n" +
                "  hurensohn INTEGER NOT NULL AUTO_INCREMENT,\n" +
                "  champ_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  match_id INTEGER NOT NULL DEFAULT 0,\n" +
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
                "  season DOUBLE NOT NULL DEFAULT 0.0,\n" +
                "  player_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  player_name VARCHAR(255) NOT NULL DEFAULT 'INVALID',\n" +
                "  region VARCHAR(255) NOT NULL DEFAULT 'INVALID',\n" +
                "  platform_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  league_tier INTEGER NOT NULL DEFAULT 0,\n" +
                "  league_points INTEGER NOT NULL DEFAULT 0,\n" +
                "  champ_level INTEGER NOT NULL DEFAULT 0,\n" +
                "  won INTEGER NOT NULL DEFAULT 0,\n" +
                "  category_id INTEGER NOT NULL DEFAULT 0,\n" +
                "  gold_earned INTEGER NOT NULL DEFAULT 0,\n" +
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
                "  killing_spree INTEGER NOT NULL DEFAULT 0,\n" +
                "  kills INTEGER NOT NULL DEFAULT 0,\n" +
                "  deaths INTEGER NOT NULL DEFAULT 0,\n" +
                "  assists INTEGER NOT NULL DEFAULT 0,\n" +
                "  damage_done BIGINT NOT NULL DEFAULT 0,\n" +
                "  damage_taken BIGINT NOT NULL DEFAULT 0,\n" +
                "  damage_shielded BIGINT NOT NULL DEFAULT 0,\n" +
                "  heal BIGINT NOT NULL DEFAULT 0,\n" +
                "  self_heal BIGINT NOT NULL DEFAULT 0,\n" +
                "  PRIMARY KEY (hurensohn),\n" +
                ");";

        if(!isConnected())
            connect();

        try(PreparedStatement statement = connection.prepareStatement(champDataTableSql)) {
            statement.executeUpdate();
            Main.DATABASE_LOGGER.info("CREATED TABLE champdata");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
