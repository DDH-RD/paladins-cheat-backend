package dev.luzifer.data.access;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.shit.ChampInfo;
import dev.luzifer.data.access.shit.DeckInfo;
import dev.luzifer.data.access.shit.GameInfo;
import dev.luzifer.data.access.shit.ItemInfo;
import dev.luzifer.data.access.shit.PlayerInfo;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.BaseObjectPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class Database {

    private static final int DUPLICATE_KEY_ERROR_CODE = 1062;
    private static final int MAX_CONNECTIONS = 10;

    private static final BasicDataSource DATA_SOURCE;

    static {
        DATA_SOURCE = setupDataSource();
    }

    private static BasicDataSource setupDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl(Webservice.getDatabaseUrl());
        basicDataSource.setUsername(Webservice.getDatabaseUsername());
        basicDataSource.setPassword(new String(Webservice.getDatabasePassword()));
        basicDataSource.setMaxTotal(MAX_CONNECTIONS);

        return basicDataSource;
    }

    public void initialize() {
        Webservice.DATABASE_LOGGER.info("Initializing database..");
        Webservice.DATABASE_LOGGER.info("Connection pool details:");
        Webservice.DATABASE_LOGGER.info(" | Database driver: " + DATA_SOURCE.getDriverClassName());
        Webservice.DATABASE_LOGGER.info(" | Database URL: " + DATA_SOURCE.getUrl());
        Webservice.DATABASE_LOGGER.info(" | Database username: " + DATA_SOURCE.getUsername());
        Webservice.DATABASE_LOGGER.info(" | Database password: " + DATA_SOURCE.getPassword().replaceAll(".", "*"));
        Webservice.DATABASE_LOGGER.info(" | Max connections: " + DATA_SOURCE.getMaxTotal());
        Webservice.DATABASE_LOGGER.info(" | Max idle connections: " + DATA_SOURCE.getMaxIdle());

        Webservice.DATABASE_LOGGER.info("Creating tables..");
        createMapInfoTable();
        createRegionInfoTable();
        createGameInfoTable();
        createPlayerInfoTable();
        createChampInfoTable();
        createBannedChampsTable();
        createDeckInfoTable();
        createItemInfoTable();

        Webservice.DATABASE_LOGGER.info("Database initialized!");
    }

    public DatabaseResult<Void> insertMapInfo(String mapName) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO MapInfo (mapName) VALUES (?)")) {
                preparedStatement.setString(1, mapName);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for mapName " + mapName,
                            DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting MapInfo record", e);
                    return new DatabaseResult<>(null, "Error inserting MapInfo record: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }

    public DatabaseResult<Void> insertBatchPlayerInfos(PlayerInfo[] playerInfos) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO PlayerInfo (playerId, playerName, region, platformId) " +
                            "VALUES (?, ?, ?, ?)")) {

                for (PlayerInfo playerInfo : playerInfos) {
                    preparedStatement.setInt(1, playerInfo.getPlayerId());
                    preparedStatement.setString(2, playerInfo.getPlayerName());
                    preparedStatement.setInt(3, playerInfo.getRegionId());
                    preparedStatement.setInt(4, playerInfo.getPlatformId());

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for playerId " + playerInfos[0].getPlayerId(),
                            DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting batch PlayerInfo records", e);
                    return new DatabaseResult<>(null, "Error inserting batch PlayerInfo records: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertBatchChampInfos(ChampInfo[] champInfos) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO ChampInfo (champId, leagueTier, leaguePoints, champLevel, " +
                            "won, categoryId, goldEarned, killingSpree, kills, deaths, assists, " +
                            "damageDone, damageTaken, damageShielded, heal, selfHeal, matchId, playerId) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                for (ChampInfo champInfo : champInfos) {
                    preparedStatement.setInt(1, champInfo.getChampId());
                    preparedStatement.setInt(2, champInfo.getLeagueTier());
                    preparedStatement.setInt(3, champInfo.getLeaguePoints());
                    preparedStatement.setInt(4, champInfo.getChampLevel());
                    preparedStatement.setInt(5, champInfo.getWon());
                    preparedStatement.setInt(6, champInfo.getCategoryId());
                    preparedStatement.setInt(7, champInfo.getGoldEarned());
                    preparedStatement.setInt(8, champInfo.getKillingSpree());
                    preparedStatement.setInt(9, champInfo.getKills());
                    preparedStatement.setInt(10, champInfo.getDeaths());
                    preparedStatement.setInt(11, champInfo.getAssists());
                    preparedStatement.setInt(12, champInfo.getDamageDone());
                    preparedStatement.setInt(13, champInfo.getDamageTaken());
                    preparedStatement.setInt(14, champInfo.getDamageShielded());
                    preparedStatement.setInt(15, champInfo.getHeal());
                    preparedStatement.setInt(16, champInfo.getSelfHeal());
                    preparedStatement.setInt(17, champInfo.getMatchId());
                    preparedStatement.setInt(18, champInfo.getPlayerId());

                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for matchId " + champInfos[0].getMatchId() +
                            " and playerId " + champInfos[0].getPlayerId(), DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting batch ChampInfo records", e);
                    return new DatabaseResult<>(null, "Error inserting batch ChampInfo records: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertBannedChamps(GameInfo gameInfo) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO BannedChamps (matchId, champId) VALUES (?, ?)")) {
                for(int champId : gameInfo.getBannedChamps()) {
                    preparedStatement.setInt(1, gameInfo.getMatchId());
                    preparedStatement.setInt(2, champId);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for matchId " + gameInfo.getMatchId(),
                            DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting BannedChamps record", e);
                    return new DatabaseResult<>(null, "Error inserting BannedChamps record: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertRegionInfo(String region) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO RegionInfo (regionName) VALUES (?)")) {
                preparedStatement.setString(1, region);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for regionName " + region,
                            DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting RegionInfo record", e);
                    return new DatabaseResult<>(null, "Error inserting RegionInfo record: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertGameInfo(GameInfo gameInfo) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO GameInfo (matchId, ranked, averageRank, mapId, " +
                            "team1Points, team2Points, duration, timestamp, " +
                            "season) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, gameInfo.getMatchId());
                preparedStatement.setInt(2, gameInfo.getRanked());
                preparedStatement.setInt(3, gameInfo.getAverageRank());
                preparedStatement.setInt(4, gameInfo.getMapId());
                preparedStatement.setInt(5, gameInfo.getTeam1Points());
                preparedStatement.setInt(6, gameInfo.getTeam2Points());
                preparedStatement.setLong(7, gameInfo.getDuration());
                preparedStatement.setLong(8, gameInfo.getTimestamp());
                preparedStatement.setDouble(9, gameInfo.getSeason());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for matchId " + gameInfo.getMatchId(),
                            DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting GameInfo record", e);
                    return new DatabaseResult<>(null, "Error inserting GameInfo record: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertBatchItemInfos(ItemInfo[] itemInfos) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO ItemInfo (item1, item2, item3, item4, " +
                            "item1Level, item2Level, item3Level, item4Level, matchId, champId) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                for (ItemInfo itemInfo : itemInfos) {
                    preparedStatement.setInt(1, itemInfo.getItem1());
                    preparedStatement.setInt(2, itemInfo.getItem2());
                    preparedStatement.setInt(3, itemInfo.getItem3());
                    preparedStatement.setInt(4, itemInfo.getItem4());
                    preparedStatement.setInt(5, itemInfo.getItem1Level());
                    preparedStatement.setInt(6, itemInfo.getItem2Level());
                    preparedStatement.setInt(7, itemInfo.getItem3Level());
                    preparedStatement.setInt(8, itemInfo.getItem4Level());
                    preparedStatement.setInt(9, itemInfo.getMatchId());
                    preparedStatement.setInt(10, itemInfo.getChampId());

                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for matchId " + itemInfos[0].getMatchId() +
                            " and champId " + itemInfos[0].getChampId(), DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting batch ItemInfo records", e);
                    return new DatabaseResult<>(null, "Error inserting batch ItemInfo records: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }
    
    public DatabaseResult<Void> insertBatchDeckInfos(DeckInfo[] deckInfos) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO DeckInfo (talentId, deckCard1, deckCard2, deckCard3, deckCard4, deckCard5, " +
                            "deckCard1Level, deckCard2Level, deckCard3Level, deckCard4Level, deckCard5Level, matchId, champId)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                for(DeckInfo deckInfo : deckInfos) {
                    preparedStatement.setInt(1, deckInfo.getTalentId());
                    preparedStatement.setInt(2, deckInfo.getDeckCard1());
                    preparedStatement.setInt(3, deckInfo.getDeckCard2());
                    preparedStatement.setInt(4, deckInfo.getDeckCard3());
                    preparedStatement.setInt(5, deckInfo.getDeckCard4());
                    preparedStatement.setInt(6, deckInfo.getDeckCard5());
                    preparedStatement.setInt(7, deckInfo.getDeckCard1Level());
                    preparedStatement.setInt(8, deckInfo.getDeckCard2Level());
                    preparedStatement.setInt(9, deckInfo.getDeckCard3Level());
                    preparedStatement.setInt(10, deckInfo.getDeckCard4Level());
                    preparedStatement.setInt(11, deckInfo.getDeckCard5Level());
                    preparedStatement.setInt(12, deckInfo.getMatchId());
                    preparedStatement.setInt(13, deckInfo.getChampId());

                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                if(e.getErrorCode() == DUPLICATE_KEY_ERROR_CODE) {
                    return new DatabaseResult<>(null, "Duplicate entry for matchId " + deckInfos[0].getMatchId() +
                            " and champId " + deckInfos[0].getChampId(), DatabaseResult.DatabaseResultType.DUPLICATE);
                } else {
                    Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error inserting batch DeckInfo records", e);
                    return new DatabaseResult<>(null, "Error inserting batch DeckInfo records: " + e.getMessage(),
                            DatabaseResult.DatabaseResultType.ERROR);
                }
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }

        return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.SUCCESS);
    }

    public DatabaseResult<List<Integer>> getBans() {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT champId FROM BannedChamps");
                List<Integer> bans = new ArrayList<>();
                while(resultSet.next()) {
                    bans.add(resultSet.getInt(1));
                }
                return new DatabaseResult<>(bans, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get bans", e);
                return new DatabaseResult<>(Collections.emptyList(), "Could not get bans: " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<List<Integer>> getBansForMap(int mapId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT champId FROM BannedChamps WHERE matchId IN " +
                            "(SELECT DISTINCT matchId FROM GameInfo WHERE mapId = ?)")) {
                preparedStatement.setInt(1, mapId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> bans = new ArrayList<>();
                while(resultSet.next()) {
                    bans.add(resultSet.getInt(1));
                }
                return new DatabaseResult<>(bans, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get bans for map " + mapId, e);
                return new DatabaseResult<>(Collections.emptyList(), "Could not get bans for map " + mapId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<List<Integer>> getBansForCategory(int categoryId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT champId FROM BannedChamps WHERE matchId IN " +
                            "(SELECT DISTINCT matchId FROM ChampInfo WHERE categoryId = ?)")) {
                preparedStatement.setInt(1, categoryId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> bans = new ArrayList<>();
                while(resultSet.next()) {
                    bans.add(resultSet.getInt(1));
                }
                return new DatabaseResult<>(bans, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get bans for category " + categoryId, e);
                return new DatabaseResult<>(Collections.emptyList(), "Could not get bans for category " + categoryId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<List<Integer>> getBansForCategoryOnMap(int categoryId, int mapId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT champId FROM BannedChamps WHERE matchId IN " +
                            "(SELECT DISTINCT matchId FROM ChampInfo WHERE categoryId = ?) AND matchId IN " +
                            "(SELECT DISTINCT matchId FROM GameInfo WHERE mapId = ?)")) {
                preparedStatement.setInt(1, categoryId);
                preparedStatement.setInt(2, mapId);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Integer> bans = new ArrayList<>();
                while(resultSet.next()) {
                    bans.add(resultSet.getInt(1));
                }
                return new DatabaseResult<>(bans, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get bans for category " + categoryId + " on map " + mapId, e);
                return new DatabaseResult<>(Collections.emptyList(), "Could not get bans for category " + categoryId + " on map " + mapId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Map<Integer, List<Integer>>> getChamps() {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.*, g.points FROM ChampInfo c INNER JOIN GameInfo g ON c.matchId = g.matchId")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                Map<Integer, List<Integer>> champInfoMap = new HashMap<>();

                while(resultSet.next()) {
                    int points = resultSet.getInt("points");
                    int champId = resultSet.getInt("champId");

                    if(!champInfoMap.containsKey(points)) {
                        champInfoMap.put(points, new ArrayList<>());
                    }

                    champInfoMap.get(points).add(champId);
                }

                return new DatabaseResult<>(champInfoMap, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get champs", e);
                return new DatabaseResult<>(Collections.emptyMap(), "Could not get champs: " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Map<Integer, List<Integer>>> getChampsOfCategory(int categoryId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.*, g.points FROM ChampInfo c INNER JOIN GameInfo g ON c.matchId = g.matchId WHERE c.categoryId = ?")) {
                preparedStatement.setInt(1, categoryId);
                ResultSet resultSet = preparedStatement.executeQuery();
                Map<Integer, List<Integer>> champInfoMap = new HashMap<>();

                while(resultSet.next()) {
                    int points = resultSet.getInt("points");
                    int champId = resultSet.getInt("champId");

                    if(!champInfoMap.containsKey(points)) {
                        champInfoMap.put(points, new ArrayList<>());
                    }

                    champInfoMap.get(points).add(champId);
                }

                return new DatabaseResult<>(champInfoMap, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get champs of category " + categoryId, e);
                return new DatabaseResult<>(Collections.emptyMap(), "Could not get champs of category " + categoryId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(Collections.emptyMap(), "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Map<Integer, List<Integer>>> getChampsOnMap(int mapId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.*, g.points FROM ChampInfo c INNER JOIN GameInfo g ON c.matchId = g.matchId WHERE g.mapId = ?")) {
                preparedStatement.setInt(1, mapId);
                ResultSet resultSet = preparedStatement.executeQuery();
                Map<Integer, List<Integer>> champInfoMap = new HashMap<>();

                while(resultSet.next()) {
                    int points = resultSet.getInt("points");
                    int champId = resultSet.getInt("champId");

                    if(!champInfoMap.containsKey(points)) {
                        champInfoMap.put(points, new ArrayList<>());
                    }

                    champInfoMap.get(points).add(champId);
                }

                return new DatabaseResult<>(champInfoMap, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get champs on map " + mapId, e);
                return new DatabaseResult<>(Collections.emptyMap(), "Could not get champs on map " + mapId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(Collections.emptyMap(), "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Map<Integer, List<Integer>>> getChampsOfCategoryOnMap(int categoryId, int mapId) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.*, g.points FROM ChampInfo c INNER JOIN GameInfo g ON c.matchId = g.matchId WHERE c.categoryId = ? AND g.mapId = ?")) {
                preparedStatement.setInt(1, categoryId);
                preparedStatement.setInt(2, mapId);
                ResultSet resultSet = preparedStatement.executeQuery();
                Map<Integer, List<Integer>> champInfoMap = new HashMap<>();

                while(resultSet.next()) {
                    int points = resultSet.getInt("points");
                    int champId = resultSet.getInt("champId");

                    if(!champInfoMap.containsKey(points)) {
                        champInfoMap.put(points, new ArrayList<>());
                    }

                    champInfoMap.get(points).add(champId);
                }

                return new DatabaseResult<>(champInfoMap, null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get champs of category " + categoryId + " on map " + mapId, e);
                return new DatabaseResult<>(Collections.emptyMap(), "Could not get champs of category " + categoryId + " on map " + mapId + ": " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(Collections.emptyMap(), "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Long> getLatestMatchId() {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            long matchId = -1;
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT matchId FROM GameInfo ORDER BY matchId DESC LIMIT 1");
                if(resultSet.next())
                    return new DatabaseResult<>(resultSet.getLong(1), null, DatabaseResult.DatabaseResultType.SUCCESS);

                return new DatabaseResult<>(matchId, null, DatabaseResult.DatabaseResultType.NOT_FOUND);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get latest match id", e);
                return new DatabaseResult<>(null, "Could not get latest match id: " + e.getMessage(),
                        DatabaseResult.DatabaseResultType.ERROR);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public void createMapInfoTable() {
        createTableIfNotExists("MapInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "mapName VARCHAR(255) UNIQUE"
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
        
        String uniqueConstraintSQL = "ALTER TABLE ChampInfo ADD CONSTRAINT unique_match_player UNIQUE (matchId, playerId)";
        executeSQL("ChampInfo", uniqueConstraintSQL);
    }
    
    public void createRegionInfoTable() {
        createTableIfNotExists("RegionInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "regionName VARCHAR(255) UNIQUE"
        );
    }

    public void createGameInfoTable() {
        createTableIfNotExists("GameInfo",
                "matchId INT PRIMARY KEY,"
                        + "ranked INT,"
                        + "averageRank INT,"
                        + "mapId INT,"
                        + "team1Points INT,"
                        + "team2Points INT,"
                        + "duration BIGINT,"
                        + "timestamp BIGINT,"
                        + "season DOUBLE"
        );
    }
    
    public void createBannedChampsTable() {
        createTableIfNotExists("BannedChamps",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "matchId INT,"
                        + "champId INT,"
                        + "FOREIGN KEY (matchId) REFERENCES GameInfo(matchId)"
        );
        
        String uniqueConstraintSQL = "ALTER TABLE BannedChamps ADD CONSTRAINT unique_match_champ UNIQUE (matchId, champId)";
        executeSQL("BannedChamps", uniqueConstraintSQL);
    }

    public void createPlayerInfoTable() {
        createTableIfNotExists("PlayerInfo",
                "playerId INT PRIMARY KEY,"
                        + "playerName VARCHAR(255),"
                        + "region INT,"
                        + "platformId INT,"
                + "FOREIGN KEY (region) REFERENCES RegionInfo(id)"
        );
    }
    
    public void createDeckInfoTable() {
        createTableIfNotExists("DeckInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
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
                        + "matchId INT,"
                        + "champId INT,"
                        + "FOREIGN KEY (matchId) REFERENCES GameInfo(matchId)"
        );
        
        String uniqueConstraintSQL = "ALTER TABLE DeckInfo ADD CONSTRAINT unique_match_champ UNIQUE (matchId, champId)";
        executeSQL("DeckInfo", uniqueConstraintSQL);
    }
    
    public void createItemInfoTable() {
        createTableIfNotExists("ItemInfo",
                "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "item1 INT,"
                        + "item2 INT,"
                        + "item3 INT,"
                        + "item4 INT,"
                        + "item1Level INT,"
                        + "item2Level INT,"
                        + "item3Level INT,"
                        + "item4Level INT,"
                        + "matchId INT,"
                        + "champId INT,"
                        + "FOREIGN KEY (matchId) REFERENCES GameInfo(matchId)"
        );
        
        String uniqueConstraintSQL = "ALTER TABLE ItemInfo ADD CONSTRAINT unique_match_champ UNIQUE (matchId, champId)";
        executeSQL("ItemInfo", uniqueConstraintSQL);
    }
    
    public DatabaseResult<Integer> getIdForRegion(String region) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            int id = -1;
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM RegionInfo WHERE regionName = ?")) {
                preparedStatement.setString(1, region);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                    return new DatabaseResult<>(resultSet.getInt(1), null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get id for region " + region, e);
                return new DatabaseResult<>(null, "Could not get id for region " + region, DatabaseResult.DatabaseResultType.ERROR);
            }

            return new DatabaseResult<>(id, null, DatabaseResult.DatabaseResultType.NOT_FOUND);
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Integer> getIdForMap(String mapName) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            int id = -1;
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM MapInfo WHERE mapName = ?")) {
                preparedStatement.setString(1, mapName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                    return new DatabaseResult<>(resultSet.getInt(1), null, DatabaseResult.DatabaseResultType.SUCCESS);
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Could not get id for map " + mapName, e);
                return new DatabaseResult<>(null, "Could not get id for map " + mapName, DatabaseResult.DatabaseResultType.ERROR);
            }

            return new DatabaseResult<>(id, null, DatabaseResult.DatabaseResultType.NOT_FOUND);
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
            return new DatabaseResult<>(null, "Error getting connection: " + e.getMessage(),
                    DatabaseResult.DatabaseResultType.ERROR);
        }
    }

    public DatabaseResult<Integer> getTotalGames() {
        return executeCountQuery("GameInfo", "Could not get total games");
    }

    public DatabaseResult<Integer> getTotalChamps() {
        return executeCountQuery("ChampInfo", "Could not get total champs");
    }

    public DatabaseResult<Integer> getTotalPlayers() {
        return executeCountQuery("PlayerInfo", "Could not get total players");
    }

    public DatabaseResult<Integer> getTotalDecks() {
        return executeCountQuery("DeckInfo", "Could not get total decks");
    }

    public DatabaseResult<Integer> getTotalItemCrafts() {
        return executeCountQuery("ItemInfo", "Could not get total item crafts");
    }

    public DatabaseResult<Integer> getTotalBannedChamps() {
        return executeCountQuery("BannedChamps", "Could not get total banned champs");
    }

    public DatabaseResult<Integer> getTotalMaps() {
        return executeCountQuery("MapInfo", "Could not get total maps");
    }

    public DatabaseResult<Integer> getTotalRegions() {
        return executeCountQuery("RegionInfo", "Could not get total regions");
    }

    private DatabaseResult<Integer> executeCountQuery(String tableName, String errorMessage) {
        try (Connection connection = DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SHOW TABLE STATUS LIKE '" + tableName + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                long rowCountEstimate = resultSet.getLong("Rows");
                return new DatabaseResult<>((int) rowCountEstimate, null, DatabaseResult.DatabaseResultType.SUCCESS);
            }

            return new DatabaseResult<>(null, null, DatabaseResult.DatabaseResultType.NOT_FOUND);
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, errorMessage, e);
            return new DatabaseResult<>(null, errorMessage, DatabaseResult.DatabaseResultType.ERROR);
        }
    }
    
    private void executeSQL(String tableName, String sql) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                Webservice.DATABASE_LOGGER.info("   | Altered table '" + tableName + "'!");
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.WARNING, "   | Key constraint for table '" + tableName + "' already exists, skipping..");
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
        }
    }

    private void createTableIfNotExists(String tableName, String tableDefinition) {
        try(Connection connection = DATA_SOURCE.getConnection()) {
            try (ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null)) {
                if (resultSet.next()) {
                    Webservice.DATABASE_LOGGER.info("| Tried to create table: " + tableName + ", but it already exists.");
                    return;
                }
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error checking if " + tableName + " table exists", e);
            }

            try (Statement statement = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableDefinition + ")";
                statement.executeUpdate(createTableSQL);
                Webservice.DATABASE_LOGGER.info(" | Created table: " + tableName + "!");
            } catch (SQLException e) {
                Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error creating " + tableName + " table", e);
            }
        } catch (SQLException e) {
            Webservice.DATABASE_LOGGER.log(Level.SEVERE, "Error getting connection", e);
        }
    }
}
