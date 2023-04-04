package dev.luzifer.data.evaluation;

import dev.luzifer.data.access.GameDao;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class BestBanForMapEvaluation {

    private final String mapName;
    private final GameDao gameDao;

    public Map<Integer, Integer> evaluate() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
