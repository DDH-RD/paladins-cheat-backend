package dev.luzifer.data.evaluation.cache;

import dev.luzifer.data.evaluation.Evaluation;

import java.util.HashMap;
import java.util.Map;

public class EvaluationCache {

    private final Map<Class<? extends Evaluation>, Object> cache = new HashMap<>();

    public <T> void cache(Evaluation<?> evaluation, T value) {
        cache.put(evaluation.getClass(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends Evaluation> evaluationClass) {
        return (T) cache.get(evaluationClass);
    }
}
