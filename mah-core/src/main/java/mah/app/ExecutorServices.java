package mah.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zgq on 2017-01-12 20:22
 */
public class ExecutorServices {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger logger = LoggerFactory.getLogger(ExecutorServices.class);

    public static Future<?> submit(Runnable task) {
        return executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                logger.error("task failed to be executed", e);
            }
        });
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                logger.error("task failed to be executed", e);
            }
            return null;
        });
    }
}
