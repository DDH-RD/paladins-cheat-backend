package dev.luzifer;

import dev.luzifer.spring.Application;
import lombok.experimental.UtilityClass;
import org.springframework.boot.SpringApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;

@UtilityClass
public class Webservice {

    public static final Logger REST_LOGGER = Logger.getLogger("Paladins-REST");
    public static final Logger DATABASE_LOGGER = Logger.getLogger("Paladins-Database");

    private static final File CREDENTIALS_FILE = new File("webservice.properties");
    private static final File LOG_FOLDER = new File("logs");

    private static String API_KEY;
    private static String DATABASE_URL;
    private static String DATABASE_USERNAME;
    private static char[] DATABASE_PASSWORD;

    static {
        redirectSystemOutToFile(buildCurrentLogFile().getAbsolutePath());
        setupCredentialsFile();
        retrieveCredentials();
    }

    public static void main(String[] args) {
        Application application = new Application();
        SpringApplication.run(application.getClass(), args);
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getDatabaseUrl() {
        return DATABASE_URL;
    }

    public static String getDatabaseUsername() {
        return DATABASE_USERNAME;
    }

    public static char[] getDatabasePassword() {
        return DATABASE_PASSWORD;
    }

    private static void setupCredentialsFile() {
        if(!CREDENTIALS_FILE.exists()) {
            REST_LOGGER.info("No credentials file found!");

            try {
                CREDENTIALS_FILE.createNewFile();
                REST_LOGGER.info("Created credentials file, " +
                        "please fill it with your credentials and restart the application!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(InputStream input = Webservice.class.getClassLoader().getResourceAsStream("webservice.properties")) {

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

            REST_LOGGER.info("Shutting down application...");
            System.exit(0);
        }
    }

    private static void retrieveCredentials() {
        REST_LOGGER.info("Found credentials file, loading credentials...");
        try {
            List<String> lines = Files.readAllLines(CREDENTIALS_FILE.toPath());
            lines.forEach(line -> {
                if(line.startsWith("api-key:")) API_KEY = line.substring(8);
                if(line.startsWith("database-url:")) DATABASE_URL = line.substring(13);
                if(line.startsWith("database-username:")) DATABASE_USERNAME = line.substring(18);
                if(line.startsWith("database-password:")) DATABASE_PASSWORD = line.substring(18).toCharArray();
            });
            REST_LOGGER.info("Loaded credentials! Booting up..");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void redirectSystemOutToFile(String logFileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(logFileName);
            OutputStream customOutputStream = new TeeOutputStream(fileOutputStream, System.out);

            System.setOut(new PrintStream(customOutputStream, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void ensureFile(File file) {
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void ensureFolder(File folder) {
        if(!folder.exists()) {
            folder.mkdir();
        }
    }

    private static File buildCurrentLogFile() {
        ensureFolder(LOG_FOLDER);

        int session = 0;
        for (File file : LOG_FOLDER.listFiles()) {
            if(file.getName().startsWith("session-")) {
                int currentSession = Integer.parseInt(file.getName().substring(8, file.getName().length() - 4));
                if(currentSession > session) session = currentSession;
            }
        }

        session++;
        File logFile = new File(LOG_FOLDER, "session-" + session + ".log");
        ensureFile(logFile);
        return logFile;
    }

    private static class TeeOutputStream extends OutputStream {
        private final OutputStream main;
        private final OutputStream second;

        public TeeOutputStream(OutputStream main, OutputStream second) {
            this.main = main;
            this.second = second;
        }

        @Override
        public void write(int b) throws IOException {
            main.write(b);
            second.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            main.write(b);
            second.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            main.write(b, off, len);
            second.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            main.flush();
            second.flush();
        }

        @Override
        public void close() throws IOException {
            main.close();
            second.close();
        }
    }
}
