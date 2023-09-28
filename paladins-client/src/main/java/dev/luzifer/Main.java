package dev.luzifer;

import dev.luzifer.application.PaladinsClient;
import javafx.application.Application;
import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Main {

    private static final File PROPERTIES_FILE = new File("paladins.properties");

    private static final Paladins PALADINS_API;

    static {
        ensurePropertiesFile();

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException e) {
            System.err.println("Could not load properties file.");
            System.exit(1);
        }

        int devId = Integer.parseInt(properties.getProperty("dev_id"));
        String authKey = properties.getProperty("auth_key");

        PALADINS_API = new PaladinsBuilder()
                .setDevId(devId)
                .setAuthKey(authKey)
                .build();
    }

    public static void main(String[] args) {
        Application.launch(PaladinsClient.class, args);
    }

    public static Paladins getPaladinsApi() {
        return PALADINS_API;
    }

    private static void ensurePropertiesFile() {
        if(!PROPERTIES_FILE.exists()) {
            System.err.println("Could not find properties file.");
            try {
                PROPERTIES_FILE.createNewFile();
                fillPropertiesFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void fillPropertiesFile() {
        try(InputStream input = Main.class.getClassLoader().getResourceAsStream("paladins.properties")) {
            InputStreamReader streamReader = new InputStreamReader(input, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);

            try(FileWriter fileWriter = new FileWriter(PROPERTIES_FILE)) {
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
