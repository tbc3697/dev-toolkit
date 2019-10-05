package pub.tbc.dev.util.base;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tbc on 2018/4/18.
 */
public class ExecutorFactory {
    private static int DEFAULT_QUEUE_SIZE = 10000;
    private static int DEFAULT_THREAD_SIZE = 1000;

    public static ThreadPoolExecutor newThreadPoolExecutor() {

        return null;
    }

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(
                nThreads,
                nThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE)
        );
    }

    public static ExecutorService newFixedThreadPool(int nThreads, int queueSize) {
        return new ThreadPoolExecutor(
                nThreads,
                nThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize)
        );
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                nThreads,
                nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE),
                threadFactory
        );
    }

    public static ExecutorService newFixedThreadPool(int nThreads, int queueSize, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                nThreads,
                nThreads,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),
                threadFactory
        );
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(DEFAULT_QUEUE_SIZE)
        );
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(
                0,
                DEFAULT_THREAD_SIZE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                0,
                DEFAULT_THREAD_SIZE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
    }

    public static ThreadFactory createThreadFactory(String nameFormat) {
        return createThreadFactory(nameFormat, false, 1);
    }

    public static ThreadFactory createThreadFactory(String nameFormat, boolean daemon, int priority) {
        String $nameFormat = nameFormat;
        boolean isDaemon = daemon;
        int $priority = priority;
        AtomicInteger threadCount = new AtomicInteger(0);
        return r -> {
            Thread thread = new Thread(r);
            thread.setName(String.format($nameFormat, threadCount.getAndIncrement()));
            thread.setDaemon(isDaemon);
            thread.setPriority($priority <= 10 ? $priority : 1);
            return thread;
        };
    }
}
