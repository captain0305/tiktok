package org.example.interact.utils;

import java.util.concurrent.*;

/**
 * @author carey
 */
public class ThreadPool {
    public static ExecutorService executor = new ThreadPoolExecutor(
        8,
        16,
        1,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(10),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy());
}
