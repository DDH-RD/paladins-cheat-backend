package dev.luzifer.algo;

public interface Evaluation<Key> {

    long[] evaluateFor(Key key, ChampType resultType);

    long[] evaluateAll(ChampType resultType);
}
