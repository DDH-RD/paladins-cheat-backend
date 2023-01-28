package dev.luzifer.data.access;

import dev.luzifer.Main;
import dev.luzifer.data.match.MatchId;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.json.JsonUtil;

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
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void delete(long id) {

        if(!isConnection())
            connect();

        String sql = "DELETE FROM Matches WHERE match_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

            Main.LOGGER.info("DELETED MATCH WITH ID:" + id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(long id, String content) {

        if(!isConnection())
            connect();

        String sql = "INSERT INTO Matches (match_id, GameInfo) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.setString(2, Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8)));
            statement.executeUpdate();

            Main.LOGGER.info("INSERTED DATA TO THE DATABASE");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMultiple(Map<MatchId, GameInfo> matches) {

        if(!isConnection())
            connect();

        String sql = "INSERT INTO Matches (match_id, GameInfo) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Map.Entry<MatchId, GameInfo> matchDetails : matches.entrySet()) {
                statement.setLong(1, matchDetails.getKey().getId());
                statement.setString(2, Base64.getEncoder().encodeToString(JsonUtil.toJson(matchDetails.getValue()).getBytes(StandardCharsets.UTF_8)));
                statement.addBatch();
            }
            statement.executeBatch();
            Main.LOGGER.info("INSERTED DATA INTO THE DATABASE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(long id) {

        if(!isConnection())
            connect();

        String sql = "SELECT GameInfo FROM Matches WHERE match_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Main.LOGGER.info("FOUND DATA FOR ID:" + id + " IN THE DATABASE");
                return new String(Base64.getDecoder().decode(result.getString("GameInfo")));
            }
            Main.LOGGER.info("FOUND NO DATA FOR:" + id);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MatchId, GameInfo> getAll() {

        if(!isConnection())
            connect();

        Map<MatchId, GameInfo> matches = new HashMap<>();
        String sql = "SELECT match_id, GameInfo FROM Matches";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet result = statement.executeQuery();
            while (result.next())
                matches.put(MatchId.of(result.getLong("match_id")),
                        JsonUtil.fromJson(new String(Base64.getDecoder().decode(result.getString("GameInfo"))), GameInfo.class));
            Main.LOGGER.info("FOUND " + matches.size() + " DATA IN THE DATABASE");
            return matches;
        } catch (SQLException e) {
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

    private void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Matches (match_id INT PRIMARY KEY, GameInfo VARCHAR(2000));";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            Main.LOGGER.info("CREATED TABLE Matches");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
