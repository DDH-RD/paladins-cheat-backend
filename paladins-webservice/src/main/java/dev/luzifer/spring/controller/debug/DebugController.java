package dev.luzifer.spring.controller.debug;

import dev.luzifer.Webservice;
import dev.luzifer.data.access.GameDao;
import dev.luzifer.data.distribution.TaskForce1;
import dev.luzifer.data.evaluation.BestBansEvaluation;
import dev.luzifer.spring.ApplicationAccessPoint;
import dev.luzifer.spring.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping(ApplicationAccessPoint.DEBUG)
public class DebugController extends AbstractController {

    @Autowired
    private GameDao gameDao;
    
    @PostMapping(ApplicationAccessPoint.FREE_MATCH_IDS)
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<?> getFreeMatchIds(@PathVariable String apiKey, @RequestBody Integer[] matchIds) {
        if(couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.info("Received unauthorized request to get free match ids.");
            
            DeferredResult<Object> result = new DeferredResult<>();
            result.setResult(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
            return result;
        }
        
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(gameDao.getFreeMatchIds(List.of(matchIds)),
                        HttpStatus.OK)),
                "Free match ids have been requested."));
        return result;
    }
    
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

                response.setHeader("Content-Disposition", "attachment; filename=logfile.txt");
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

    @CrossOrigin(origins = "*")
    @GetMapping(ApplicationAccessPoint.ALL_MAPS)
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<?> getAllMaps(@PathVariable String apiKey) {
        if(couldNotVerifyApiKey(apiKey)) {
            Webservice.REST_LOGGER.info("Received unauthorized request to get all maps.");

            DeferredResult<Object> result = new DeferredResult<>();
            result.setResult(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
            return result;
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        TaskForce1.order(() -> timing(
                () -> result.setResult(new ResponseEntity<>(gameDao.getAllMaps(),
                        HttpStatus.OK)),
                "All maps have been requested."));
        return result;
    }
}
