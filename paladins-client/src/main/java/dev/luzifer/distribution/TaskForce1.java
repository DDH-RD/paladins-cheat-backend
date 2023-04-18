package dev.luzifer.distribution;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

@UtilityClass
public class TaskForce1 {

    private static final Logger LOGGER = Logger.getLogger(TaskForce1.class.getName());
    
    private static final Executor TASK_EXECUTOR =
            new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors() - 2,
                    Runtime.getRuntime().availableProcessors(),
                    250L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new ThreadFactoryBuilder()
                            .setUncaughtExceptionHandler(new UncaughtExplosionHandler())
                            .setDaemon(true)
                            .setNameFormat("Paladins Task-%d")
                            .build(),
                    new ThreadPoolExecutor.AbortPolicy());

    public static CompletableFuture order(Runnable task) {
        return CompletableFuture.runAsync(task, TASK_EXECUTOR);
    }

    public static <T> CompletableFuture order(Supplier<T> task, Consumer<T> callback) {
        return CompletableFuture.supplyAsync(task, TASK_EXECUTOR).thenAccept(callback);
    }

    public static Executor getTaskExecutor() {
        return TASK_EXECUTOR;
    }

    public static class ChainedTask implements Runnable {

        private final Deque<Runnable> tasks = new ArrayDeque<>();
        private final Runnable onDone;

        public ChainedTask(Runnable onDone) {
            this.onDone = onDone;
        }

        public void addTask(Runnable task) {
            tasks.add(task);
        }

        public <T> void addTask(Supplier<T> task, Consumer<T> callback) {
            tasks.add(() -> callback.accept(task.get()));
        }

        @Override
        public void run() {
            CompletableFuture<Void> allTasksDone = CompletableFuture.allOf(
                    tasks.stream()
                            .map(TaskForce1::order)
                            .toArray(CompletableFuture[]::new)
            );
            allTasksDone.thenRun(onDone).join();
        }
    }

    private static class UncaughtExplosionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            LOGGER.severe("Uncaught exception in thread " + t.getName() + ": " + e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                LOGGER.severe(element.toString());
            }
        }
    }
}
