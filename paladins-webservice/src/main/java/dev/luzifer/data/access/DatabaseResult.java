package dev.luzifer.data.access;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Optional;

@Value
@RequiredArgsConstructor
public class DatabaseResult<T> {

    T result;
    String message;
    DatabaseResultType databaseResultType;

    public Optional<T> getResult() {
        return Optional.ofNullable(result);
    }

    public enum DatabaseResultType {
        SUCCESS,
        DUPLICATE,
        NOT_FOUND, // steht auch für not inserted
        ERROR
    }
}
