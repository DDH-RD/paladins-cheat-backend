package dev.luzifer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Credentials {

    private static final File CREDENTIALS_FILE = new File("webservice.properties");

    public static Credentials get() {
        return new Credentials();
    }

    private String apiKey;
    private String databaseUrl;
    private String databaseUsername;
    private char[] databasePassword;

    private Credentials() {
        initialize();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public char[] getDatabasePassword() {
        return databasePassword;
    }

    private void initialize() {
        if(!CREDENTIALS_FILE.exists()) {

            try {
                CREDENTIALS_FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(InputStream input = Main.class.getClassLoader().getResourceAsStream("webservice.properties")) {

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

        if(apiKey == null || databaseUrl == null || databaseUsername == null || databasePassword == null) {
            try {
                List<String> lines = Files.readAllLines(CREDENTIALS_FILE.toPath());
                lines.forEach(line -> {
                    if(line.startsWith("api-key:")) apiKey = line.substring(8);
                    if(line.startsWith("database-url:")) databaseUrl = line.substring(13);
                    if(line.startsWith("database-username:")) databaseUsername = line.substring(18);
                    if(line.startsWith("database-password:")) databasePassword = line.substring(18).toCharArray();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
