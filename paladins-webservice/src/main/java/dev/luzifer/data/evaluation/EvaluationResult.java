package dev.luzifer.data.evaluation;

public class EvaluationResult<T> {

    private final T t;

    public EvaluationResult(T t) {
        this.t = t;
    }

    public T getResult() {
        return t;
    }
}
