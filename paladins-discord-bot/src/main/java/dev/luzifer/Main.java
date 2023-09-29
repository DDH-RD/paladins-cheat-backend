package dev.luzifer;

import dev.luzifer.webclient.Webclient;
import dev.luzifer.webclient.WebclientCredentials;
import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.PaladinsBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

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
    private static final Webclient WEBCLIENT;
    private static final JDA JDA;

    static {
        ensurePropertiesFile();

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException e) {
            System.err.println("Could not load properties file.");
            System.exit(1);
        }

        PALADINS_API = new PaladinsBuilder()
                .setDevId(Integer.parseInt(properties.getProperty("dev_id")))
                .setAuthKey(properties.getProperty("auth_key"))
                .build();

        WEBCLIENT = new Webclient(new WebclientCredentials(
                properties.getProperty("api_key"),
                properties.getProperty("ip_address")));

        JDA = JDABuilder.createLight(properties.getProperty("discord_token"),
                        GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.streaming("Paladins", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley"))
                .setStatus(OnlineStatus.IDLE)
                .build();
    }

    public static void main(String[] args) {
    }

    public static Paladins getPaladinsApi() {
        return PALADINS_API;
    }

    public static Webclient getWebclient() {
        return WEBCLIENT;
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
