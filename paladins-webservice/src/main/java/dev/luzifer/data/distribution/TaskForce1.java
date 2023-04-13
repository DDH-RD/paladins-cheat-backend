package dev.luzifer.data.distribution;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@UtilityClass
public class TaskForce1 {

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

    public static void order(Runnable task) {
        TASK_EXECUTOR.execute(task);
    }

    public static <T> void order(Supplier<T> task, Consumer<T> callback) {
        TASK_EXECUTOR.execute(() -> callback.accept(task.get()));
    }

    public static Executor getTaskExecutor() {
        return TASK_EXECUTOR;
    }

    public static class ChainedTask implements Runnable {

        private static final int TIMEOUT = 5000;

        private final Deque<Runnable> tasks = new ArrayDeque<>();

        private volatile long lastUpdate = 0;

        public void addTask(Runnable task) {
            tasks.add(task);
            lastUpdate = System.currentTimeMillis();
        }

        public <T> void addTask(Supplier<T> task, Consumer<T> callback) {
            tasks.add(() -> callback.accept(task.get()));
            lastUpdate = System.currentTimeMillis();
        }

        @Override
        public void run() {
            while (System.currentTimeMillis() - lastUpdate < TIMEOUT) {
                if(tasks.isEmpty()) continue;
                Runnable task = tasks.pop();
                task.run();
            }
        }
    }

    private static class UncaughtExplosionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Uncaught explosion in thread '" + t.getName() + "': " + e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                System.out.println("    " + element.toString());
            }
        }
    }
}
