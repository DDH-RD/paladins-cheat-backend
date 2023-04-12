package dev.luzifer.data.access;

import dev.luzifer.data.distribution.TaskForce1;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SqlQueryDistributor {

    private static final int LIMIT = 20000;

    private final Database database;
    private final int expectedNumberOfEntries;
    private final String query;

    private volatile int currentOffset = 0;

    public SqlQueryDistributor(Database database, String query, int amount) {
        this.database = database;
        this.query = query;
        this.expectedNumberOfEntries = amount;
    }

    public CompletableFuture<List<ResultSet>> distribute() {
        return CompletableFuture.supplyAsync(() -> {
            int estimatedNumberOfQueries = getEstimatedNumberOfQueries();
            List<ResultSet> resultSets = new ArrayList<>(estimatedNumberOfQueries);
            int index = 0;
            while (index < estimatedNumberOfQueries) {
                TaskForce1.order(() -> {
                    String sqlQuery = modifyQuery();
                    return database.execute(sqlQuery);
                }, resultSets::add);
                index++;
            }
            return resultSets;
        }, TaskForce1.getTaskExecutor());
    }

    private int getEstimatedNumberOfQueries() {
        return expectedNumberOfEntries / LIMIT;
    }

    private String modifyQuery() {
        String sqlQuery = query;
        int offset = getCurrentOffset();
        sqlQuery += " LIMIT " + LIMIT + " OFFSET " + offset;
        incrementCurrentOffset();
        return sqlQuery;
    }

    private synchronized void incrementCurrentOffset() {
        currentOffset += LIMIT;
    }

    private synchronized int getCurrentOffset() {
        return currentOffset;
    }
}
