package dev.luzifer.data;

import dev.luzifer.Main;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class WallHack {

    private static final Queue<Runnable> TASKS = new LinkedBlockingQueue<>();
    private static final Thread WORKER;

    static {
        WORKER = new Thread(() -> {
            while (true) {
                try {
                    if (TASKS.isEmpty()) {
                        Thread.sleep(2000);
                        continue;
                    }
                    TASKS.poll().run();
                } catch (InterruptedException e) {
                    Main.LOGGER.warning("Async Worker interrupted. Trying to restart...");
                    tryToRestartWorkerAfterCrash();
                }
            }
        });
        WORKER.setDaemon(true);
        WORKER.setName("Async Worker");
        WORKER.start();
    }

    public static void submitTask(Runnable runnable) {
        TASKS.add(runnable);
    }

    private static void tryToRestartWorkerAfterCrash() {
        int attempts = 0;
        while (attempts < 30 && !WORKER.isAlive()) {
            try {
                WORKER.start();
                attempts++;
                Main.LOGGER.info("Trying to restart Async Worker... [" + attempts + "/30]");
                Thread.sleep(100);
            } catch (IllegalThreadStateException e) {
                Main.LOGGER.severe("Failed to restart Async Worker after 30 attempts.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Main.LOGGER.info("Async Worker state: " + WORKER.isAlive());
    }
}
