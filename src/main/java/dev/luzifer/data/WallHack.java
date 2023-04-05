package dev.luzifer.data;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class WallHack {

    private static final Queue<Runnable> TASKS = new LinkedBlockingQueue<>();
    private static final Thread WORKER;

    static {
        WORKER = new Thread(() -> {
            while (true) {
                try {
                    if(TASKS.isEmpty()) {
                        Thread.sleep(2000);
                        continue;
                    }
                    TASKS.poll().run();
                } catch (Exception e) {
                    e.printStackTrace();
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
}
