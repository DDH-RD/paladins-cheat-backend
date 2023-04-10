package dev.luzifer.data.access;

import dev.luzifer.Main;
import dev.luzifer.data.match.info.ChampData;
import dev.luzifer.data.match.info.ChampDto;
import dev.luzifer.data.match.info.GameDto;
import dev.luzifer.spring.controller.GameController;
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

    private Connection connection;

    public void insert(GameDto[] games) {

        if(!isConnection())
            connect();

        String sql = "INSERT IGNORE INTO games (id, map_name, ranked, average_rank, banned_champ1, banned_champ2, banned_champ3, banned_champ4, banned_champ5, banned_champ6, team1_points, team2_points, duration, timestamp, season) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement champStatement = connection.prepareStatement(
                     "INSERT IGNORE INTO champs (id, match_id, player_id, player_name, region, platform_id, league_tier, league_points, champ_level, won, category_id, gold_earned, talent_id, deck_card1, deck_card2, deck_card3, deck_card4, deck_card5, deck_card1_level, deck_card2_level, deck_card3_level, deck_card4_level, deck_card5_level, item1, item2, item3, item4, item1Level, item2Level, item3Level, item4Level, killing_spree, kills, deaths, assists, damage_done, damage_taken, damage_shielded, heal, self_heal) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {

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
                    gamesStatement.setDouble(15, gameDto.getSeason());
                    gamesStatement.addBatch();

                    for (ChampDto champ : gameDto.getChamps()) {
                        champStatement.setInt(1, champ.getChamp_id());
                        champStatement.setInt(2, gameDto.getId());
                        champStatement.setInt(3, champ.getPlayerId());
                        champStatement.setString(4, champ.getPlayerName());
                        champStatement.setString(5, champ.getRegion());
                        champStatement.setInt(6, champ.getPlatformId());
                        champStatement.setInt(7, champ.getLeagueTier());
                        champStatement.setInt(8, champ.getLeaguePoints());
                        champStatement.setInt(9, champ.getChampLevel());
                        champStatement.setInt(10, champ.getWon());
                        champStatement.setInt(11, champ.getCategoryId());
                        champStatement.setLong(12, champ.getGoldEarned());
                        champStatement.setInt(13, champ.getTalentId());
                        champStatement.setInt(14, champ.getDeckCard1());
                        champStatement.setInt(15, champ.getDeckCard2());
                        champStatement.setInt(16, champ.getDeckCard3());
                        champStatement.setInt(17, champ.getDeckCard4());
                        champStatement.setInt(18, champ.getDeckCard5());
                        champStatement.setInt(19, champ.getDeckCard1Level());
                        champStatement.setInt(20, champ.getDeckCard2Level());
                        champStatement.setInt(21, champ.getDeckCard3Level());
                        champStatement.setInt(22, champ.getDeckCard4Level());
                        champStatement.setInt(23, champ.getDeckCard5Level());
                        champStatement.setInt(24, champ.getItem1());
                        champStatement.setInt(25, champ.getItem2());
                        champStatement.setInt(26, champ.getItem3());
                        champStatement.setInt(27, champ.getItem4());
                        champStatement.setInt(28, champ.getItem1Level());
                        champStatement.setInt(29, champ.getItem2Level());
                        champStatement.setInt(30, champ.getItem3Level());
                        champStatement.setInt(31, champ.getItem4Level());
                        champStatement.setInt(32, champ.getKillingSpree());
                        champStatement.setInt(33, champ.getKills());
                        champStatement.setInt(34, champ.getDeaths());
                        champStatement.setInt(35, champ.getAssists());
                        champStatement.setLong(36, champ.getDamageDone());
                        champStatement.setLong(37, champ.getDamageTaken());
                        champStatement.setLong(38, champ.getDamageShielded());
                        champStatement.setLong(39, champ.getHeal());
                        champStatement.setLong(40, champ.getSelfHeal());
                        champStatement.addBatch();
                    }
                }
                gamesStatement.executeBatch();
                champStatement.executeBatch();
                Main.DATABASE_LOGGER.info("INSERTED A BATCH WITH " + games.length + " ENTRIES INTO THE DATABASE");
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

    public void migrate() {

        if(!isConnection())
            connect();

        // fetch all games
        GameDto[] games = fetchGames(GameController.MatchType.ALL);

        // push it up
        int index = 0;
        for(GameDto gameDto : games) {
            ChampData[] champData = constructChampData(gameDto);
            insertChampData(champData);

            Main.DATABASE_LOGGER.info("MIGRATED GAME " + gameDto.getId() + " [" + index++ + "/" + games.length + " (" + (index / games.length) * 100 + "%)]");
        }

        Main.DATABASE_LOGGER.info("MIGRATED " + games.length + " GAMES TO THE NEW DATABASE");
    }

    private ChampData[] constructChampData(GameDto gameDto) {
        ChampData[] champData = new ChampData[10];
        int index = 0;
        for (ChampDto champDto : gameDto.getChamps()) {
            champData[index] = new ChampData(
                    gameDto.getId(),
                    gameDto.getMapName(),
                    gameDto.getRanked(),
                    gameDto.getAverageRank(),
                    gameDto.getBannedChamp1(),
                    gameDto.getBannedChamp2(),
                    gameDto.getBannedChamp3(),
                    gameDto.getBannedChamp4(),
                    gameDto.getBannedChamp5(),
                    gameDto.getBannedChamp6(),
                    gameDto.getTeam1Points(),
                    gameDto.getTeam2Points(),
                    gameDto.getDuration(),
                    gameDto.getTimestamp(),
                    gameDto.getSeason(),
                    champDto.getChamp_id(),
                    champDto.getPlayerId(),
                    champDto.getPlayerName(),
                    champDto.getRegion(),
                    champDto.getPlatformId(),
                    champDto.getLeagueTier(),
                    champDto.getLeaguePoints(),
                    champDto.getChampLevel(),
                    champDto.getWon(),
                    champDto.getCategoryId(),
                    champDto.getGoldEarned(),
                    champDto.getTalentId(),
                    champDto.getDeckCard1(),
                    champDto.getDeckCard2(),
                    champDto.getDeckCard3(),
                    champDto.getDeckCard4(),
                    champDto.getDeckCard5(),
                    champDto.getDeckCard1Level(),
                    champDto.getDeckCard2Level(),
                    champDto.getDeckCard3Level(),
                    champDto.getDeckCard4Level(),
                    champDto.getDeckCard5Level(),
                    champDto.getItem1(),
                    champDto.getItem2(),
                    champDto.getItem3(),
                    champDto.getItem4(),
                    champDto.getItem1Level(),
                    champDto.getItem2Level(),
                    champDto.getItem3Level(),
                    champDto.getItem4Level(),
                    champDto.getKillingSpree(),
                    champDto.getKills(),
                    champDto.getDeaths(),
                    champDto.getAssists(),
                    champDto.getDamageDone(),
                    champDto.getDamageTaken(),
                    champDto.getDamageShielded(),
                    champDto.getHeal(),
                    champDto.getSelfHeal()
            );
        }
        return champData;
    }

    public void insertChampData(ChampData[] champData) {
        if(!isConnection())
            connect();

        ensureTableExists();
        String sql = "INSERT INTO champdata (champ_id, match_id, map_name, ranked, average_rank, banned_champ1, banned_champ2, banned_champ3, banned_champ4, banned_champ5, banned_champ6, team1_points, team2_points, duration, timestamp, season, player_id, player_name, region, platform_id, league_tier, league_points, champ_level, won, category_id, gold_earned, talent_id, deck_card1, deck_card2, deck_card3, deck_card4, deck_card5, deck_card1_level, deck_card2_level, deck_card3_level, deck_card4_level, deck_card5_level, item1, item2, item3, item4, item1Level, item2Level, item3Level, item4Level, killing_spree, kills, deaths, assists, damage_done, damage_taken, damage_shielded, heal, self_heal)\n" +
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
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert champ data", e);
        }
    }

    public boolean isConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countEntriesOnMap(String mapName, GameController.MatchType matchType) {
        if(!isConnection())
            connect();

        String sql = "SELECT COUNT(*) FROM games WHERE map_name = ?";
        switch (matchType) {
            case RANKED:
                sql += " AND ranked = 1";
                break;
            case CASUAL:
                sql += " AND ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mapName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Main.DATABASE_LOGGER.info("COUNTED " + resultSet.getInt(1) + " ENTRIES ON MAP " + mapName + " IN THE DATABASE");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int countEntriesWithChamp(int champId, GameController.MatchType matchType) {
        if(!isConnection())
            connect();

        String sql = "SELECT COUNT(*) FROM games WHERE id IN (SELECT game_id FROM champs WHERE champ_id = ?)";
        switch (matchType) {
            case RANKED:
                sql += " AND ranked = 1";
                break;
            case CASUAL:
                sql += " AND ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, champId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Main.DATABASE_LOGGER.info("COUNTED " + resultSet.getInt(1) + " ENTRIES WITH CHAMP " + champId + " IN THE DATABASE");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int countEntriesWithChampOnMap(int champId, String mapName, GameController.MatchType matchType) {
        if(!isConnection())
            connect();

        String sql = "SELECT COUNT(*) FROM games WHERE map_name = ? AND id IN (SELECT game_id FROM champs WHERE champ_id = ?)";
        switch (matchType) {
            case RANKED:
                sql += " AND ranked = 1";
                break;
            case CASUAL:
                sql += " AND ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mapName);
            statement.setInt(2, champId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Main.DATABASE_LOGGER.info("COUNTED " + resultSet.getInt(1) + " ENTRIES WITH CHAMP " + champId + " ON MAP " + mapName + " IN THE DATABASE");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int countEntries(GameController.MatchType matchType) {

        if(!isConnection())
            connect();

        String sql = "SELECT COUNT(*) FROM games";
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
                Main.DATABASE_LOGGER.info("COUNTED " + resultSet.getInt(1) + " ENTRIES IN THE DATABASE");
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public GameDto[] fetchGamesWithChamp(GameController.MatchType matchType, int champId) {
        if(!isConnection()) {
            connect();
        }

        String sql = "SELECT * FROM games WHERE id IN (SELECT match_id FROM champs WHERE id = ?)";
        switch (matchType) {
            case RANKED:
                sql += " AND ranked = 1";
                break;
            case CASUAL:
                sql += " AND ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, champId);
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
                        resultSet.getLong("timestamp"),
                        resultSet.getDouble("season"));
                games.add(game);
            }
            Main.DATABASE_LOGGER.info("FETCHED " + games.size() + " GAMES WITH CHAMP " + champId + " FROM THE DATABASE");
            return games.toArray(new GameDto[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameDto[] fetchGamesOnMap(GameController.MatchType matchType, String mapName) {
        if (!isConnection()) {
            connect();
        }

        String sql = "SELECT * FROM games WHERE map_name = ?";
        switch (matchType) {
            case RANKED:
                sql += " AND ranked = 1";
                break;
            case CASUAL:
                sql += " AND ranked = 0";
                break;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mapName);
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
                        resultSet.getLong("timestamp"),
                        resultSet.getDouble("season"));
                games.add(game);
            }
            Main.DATABASE_LOGGER.info("FETCHED " + games.size() + " GAMES FROM THE DATABASE");
            return games.toArray(new GameDto[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GameDto[] fetchGames(GameController.MatchType matchType) {
        if (!isConnection()) {
            connect();
        }

        String sql = "SELECT * FROM games";
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
                        resultSet.getLong("timestamp"),
                        resultSet.getDouble("season"));

                games.add(game);
            }
            Main.DATABASE_LOGGER.info("FETCHED " + games.size() + " GAMES FROM THE DATABASE");
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
                        resultSet.getInt("item1Level"),
                        resultSet.getInt("item2Level"),
                        resultSet.getInt("item3Level"),
                        resultSet.getInt("item4Level"),
                        resultSet.getInt("killing_spree"),
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

        if(!isConnection())
            connect();

        try(PreparedStatement statement = connection.prepareStatement(champDataTableSql)) {
            statement.executeUpdate();
            Main.DATABASE_LOGGER.info("CREATED TABLE champdata");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
