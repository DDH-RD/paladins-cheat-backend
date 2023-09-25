package dev.luzifer.spring.controller;

import dev.luzifer.Webservice;
import dev.luzifer.spring.ApplicationAccessPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@RestController
@RequestMapping(ApplicationAccessPoint.DEBUG)
public class DebugController {

    @GetMapping(ApplicationAccessPoint.LATEST_LOG_DOWNLOAD)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> downloadLatestLog(@PathVariable String apiKey, HttpServletResponse response) {
        if(couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.info("Received unauthorized request to download latest log.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        File logFile = Webservice.getCURRENT_LOG_FILE();

        if (logFile != null && logFile.exists()) {
            try {
                byte[] logBytes = Files.readAllBytes(logFile.toPath());

                response.setHeader("Content-Disposition", "attachment; filename=logfile.log");
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

                OutputStream outputStream = response.getOutputStream();
                outputStream.write(logBytes);
                outputStream.flush();
                outputStream.close();

                return ResponseEntity.ok().build();
            } catch (IOException e) {
                Webservice.REST_LOGGER.severe("Error while downloading latest log.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Webservice.REST_LOGGER.severe("Could not find latest log file.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private boolean couldNotVerifyApiKey(String key) {
        return !Webservice.getApiKey().equals(key);
    }
}
