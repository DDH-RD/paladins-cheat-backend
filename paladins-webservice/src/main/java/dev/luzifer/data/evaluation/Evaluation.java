package dev.luzifer.data.evaluation;

public interface Evaluation<T> {

    T evaluate();

    T evaluate(int champCategory);
}
