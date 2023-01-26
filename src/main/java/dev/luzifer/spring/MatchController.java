package dev.luzifer.spring;

import dev.luzifer.Main;
import dev.luzifer.WebPath;
import dev.luzifer.data.access.MatchDao;
import dev.luzifer.data.match.MatchMapper;
import dev.luzifer.data.match.info.GameInfo;
import dev.luzifer.data.match.MatchId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.util.Map;

@Controller(WebPath.MATCH)
public class MatchController {

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private MatchDao matchDao;

    @PostMapping(WebPath.POST_MATCH_INFO)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postMatchInfo(@PathVariable long id, @RequestBody GameInfo gameInfo) {

        Main.LOGGER.info(MessageFormat.format("RECEIVED POST REQUEST WITH ID:{0} AND GAMEINFO:{1}", id, gameInfo));

        MatchId matchId = MatchId.of(id);
        matchMapper.map(matchId, gameInfo);
        matchDao.save(matchId, gameInfo);
    }
    
    @PostMapping(WebPath.POST_MULTIPLE_MATCH_INFO)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void postMultipleMatches(Map<MatchId, GameInfo> map) {
        
        Main.LOGGER.info(MessageFormat.format("RECEIVED POST REQUEST WITH {0} DATA.", map.size()));
        
        matchMapper.mapAll(map);
        matchDao.saveMultiple(map);
    }
    
    @GetMapping(value = WebPath.GET_MATCH_IDS, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<MatchId[]> getMatchIds() {

        Main.LOGGER.info("RECEIVED GET REQUEST FOR ALL MATCH IDS");

        MatchId[] matchIds = matchMapper.getMappings().keySet().toArray(new MatchId[0]);
        return new ResponseEntity<>(matchIds, HttpStatus.OK);
    }

    @GetMapping(value = WebPath.GET_MATCH_INFO, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<GameInfo> getMatchInfo(@PathVariable long id) {

        Main.LOGGER.info(MessageFormat.format("RECEIVED GET REQUEST WITH ID:{0}", id));

        return new ResponseEntity<>(matchMapper.getMapping(MatchId.of(id)), HttpStatus.OK);
    }

}
