package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.data.MatchMapper;
import dev.luzifer.data.gamestats.GameInfo;
import dev.luzifer.data.gamestats.MatchId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;

@Controller
public class PaladinsController {

    private final MatchMapper matchMapper = new MatchMapper();

    @PostMapping(WebPath.PALADINS_POST_MATCH_INFO)
    public void postMatchInfo(@PathVariable long id, @RequestBody GameInfo gameInfo) {
        Main.LOGGER.info(MessageFormat.format("RECEIVED POST REQUEST WITH ID:{0} AND GAMEINFO:{1}", id, gameInfo.toString()));
        matchMapper.map(MatchId.of(id), gameInfo);
    }
    
    @GetMapping(value = WebPath.PALADINS_GET_MATCH_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<GameInfo> postMatchInfo(@PathVariable long id) {
        Main.LOGGER.info(MessageFormat.format("RECEIVED GET REQUEST WITH ID:{0}", id));
        return new ResponseEntity<>(matchMapper.getMapping(MatchId.of(id)), HttpStatus.OK);
    }

}
