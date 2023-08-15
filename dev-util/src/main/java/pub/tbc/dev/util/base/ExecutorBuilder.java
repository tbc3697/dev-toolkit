package pub.tbc.dev.util.base;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 线程池构建器，暂只支持构建普通线程池，线程不安全，应在方法内使用
 *
 * @Author tbc by 2019/2/27 5:21 下午
 */
@Data
@Slf4j
@Accessors(fluent = true)
public class ExecutorBuilder {
    private static final String DEF_THREAD_NAME_PRE = "commonThread";
    private static final String DEF_THREAD_NAME_SEPARATOR = "-";
    /**
     * 线程数量
     */
    private int coreThread;
    private int maxThread;

    /**
     * 超时销毁
     */
    private long keepAliveTime = 0L;
    private TimeUnit keepAliveTimeUnit = TimeUnit.MILLISECONDS;

    /**
     * 任务队列
     */
    private int queueCapacity = 10_0000;
    private BlockingQueue<Runnable> blockingQueue;

    /**
     * 线程工厂、命名
     */
    private String threadNamePrefix;
    private String threadNameSuffix;
    private ThreadFactory threadFactory;

    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler rejectedHandler = loggingAbortPolicy();
    /**
     * 是否回收超时的 coreThread
     */
    private boolean allowCoreThreadTimeOut = false;


    private int availableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    private BlockingQueue createQueue() {
        return new LinkedBlockingQueue(queueCapacity);
    }

    private BlockingQueue getQueue() {
        return blockingQueue == null ? createQueue() : blockingQueue;
    }

    private ThreadFactory getThreadFactory() {
        return new ThreadFactory() {
            private AtomicInteger threadCounter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, getThreadName(threadCounter));
            }
        };
    }

    private String getThreadName(AtomicInteger threadCounter) {
        StringBuilder builder = new StringBuilder();
        if (threadNamePrefix != null && !threadNamePrefix.isEmpty()) {
            builder.append(threadNamePrefix);
        } else {
            builder.append(DEF_THREAD_NAME_PRE);
        }
        builder.append(DEF_THREAD_NAME_SEPARATOR);
        builder.append(threadCounter.getAndIncrement());
        if (threadNameSuffix != null && !threadNameSuffix.isEmpty()) {
            builder.append(threadNameSuffix);
        }
        return builder.toString();
    }

    public ThreadPoolExecutor build() {
        int core = coreThread;
        int max = maxThread;
        if (core == 0 || max == 0) {
            int availableProcessors = availableProcessors();
            core = core == 0 ? availableProcessors / 2 : core;
            max = max == 0 ? availableProcessors : max;
        }
        long k = keepAliveTime;
        TimeUnit unit = keepAliveTimeUnit;
        BlockingQueue queue = getQueue();
        ThreadFactory t = getThreadFactory();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(core, max, k, unit, queue, t, rejectedHandler);
        // Core threads must have nonzero keep alive times
        if (k != 0 && allowCoreThreadTimeOut) {
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        }
        return threadPoolExecutor;
    }


    // build factory

    public static ExecutorBuilder buildFixedThreadPool(int threadCount, ThreadFactory factory) {
        return new ExecutorBuilder().threadFactory(factory).coreThread(threadCount).maxThread(threadCount);
    }

    public static ExecutorBuilder buildFixedThreadPool(int threadCount, String threadNamePrefix) {
        return buildFixedThreadPool(threadCount, threadNamePrefix, null);
    }

    public static ExecutorBuilder buildFixedThreadPool(int threadCount, String threadNamePrefix, String threadNameSuffix) {
        return new ExecutorBuilder()
                .threadNamePrefix(threadNamePrefix)
                .threadNameSuffix(threadNameSuffix)
                .coreThread(threadCount)
                .maxThread(threadCount);
    }

    public static ExecutorBuilder buildSingleThreadExecutor(String threadNamePrefix) {
        return buildFixedThreadPool(1, threadNamePrefix);
    }

    ////// ************* ///////

    public static ThreadPoolExecutor newFixedThreadPool(int threadCount, ThreadFactory factory) {
        return new ExecutorBuilder().threadFactory(factory).coreThread(threadCount).maxThread(threadCount).build();
    }

    public static ThreadPoolExecutor newFixedThreadPool(int threadCount, String threadNamePrefix) {
        return newFixedThreadPool(threadCount, threadNamePrefix, null);
    }

    public static ThreadPoolExecutor newFixedThreadPool(int threadCount, String threadNamePrefix, String threadNameSuffix) {
        return new ExecutorBuilder()
                .threadNamePrefix(threadNamePrefix)
                .threadNameSuffix(threadNameSuffix)
                .coreThread(threadCount)
                .maxThread(threadCount)
                .build();
    }

    public static ThreadPoolExecutor newSingleThreadExecutor(String threadNamePrefix) {
        return newFixedThreadPool(1, threadNamePrefix);
    }


    // RejectedExecutor begin

    public static RejectedExecutionHandler loggingAbortPolicy() {
        return (Runnable r, ThreadPoolExecutor e) -> {
            rejectedLogging(e, "抛出异常（Abort）");
            throw new RejectedExecutionException(String.format(
                    "Task %s rejected from %s", r.toString(), e.toString()
            ));
        };
    }

    public static RejectedExecutionHandler loggingCallerRunsPolicy() {
        return (Runnable r, ThreadPoolExecutor e) -> {
            rejectedLogging(e, "主线程执行任务（CallerRuns）");
            if (!e.isShutdown()) {
                r.run();
            }
        };
    }

    public static RejectedExecutionHandler loggingDiscardPolicy() {
        return (Runnable r, ThreadPoolExecutor e) -> rejectedLogging(e, "丢弃任务（Discard）");
    }

    public static RejectedExecutionHandler loggingDiscardOldestPolicy() {
        return (Runnable r, ThreadPoolExecutor e) -> {
            rejectedLogging(e, "丢弃最早的任务（DiscardOld）");
            if (!e.isShutdown()) {
                e.getQueue().poll();
                e.execute(r);
            }
        };
    }

    private static void rejectedLogging(ThreadPoolExecutor e, String msg) {
        log.error("[线程池满,拒绝策略：{}] 任务总数：{}，已完成数：{}, 队列大小：{}, 活动线程数：{}, 核心线程数：{}, isShutdown：{}",
                msg,
                e.getTaskCount(),
                e.getCompletedTaskCount(),
                e.getQueue().size(),
                e.getActiveCount(),
                e.getCorePoolSize(),
                e.isShutdown()
        );
    }


    // rejected class

    // private static class LoggingAbortPolicy implements RejectedExecutionHandler {
    //     @Override
    //     public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    //         rejectedLogging(e, "抛出异常（Abort）");
    //         throw new RejectedExecutionException(String.format(
    //                 "Task %s rejected from %s", r.toString(), e.toString()
    //         ));
    //     }
    // }
    // private static class LoggingCallerRunsPolicy implements RejectedExecutionHandler {
    //     @Override
    //     public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    //         rejectedLogging(e, "主线程执行任务（CallerRuns）");
    //         if (!e.isShutdown()) {
    //             r.run();
    //         }
    //     }
    // }
    //
    // private static class LoggingDiscardPolicy implements RejectedExecutionHandler {
    //     @Override
    //     public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    //         rejectedLogging(e, "丢弃任务（Discard）");
    //     }
    // }
    //
    // private static class LoggingDiscardOldestPolicy implements RejectedExecutionHandler {
    //     @Override
    //     public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    //         rejectedLogging(e, "丢弃最早的任务（DiscardOld）");
    //         if (!e.isShutdown()) {
    //             e.getQueue().poll();
    //             e.execute(r);
    //         }
    //     }
    // }

    // RejectedExecutor end

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor test = new ExecutorBuilder()
                .coreThread(2)
                .maxThread(4)
                .threadNamePrefix("test")
                .keepAliveTime(1)
                .allowCoreThreadTimeOut(true)
                .build();
        Consumer r = serial -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "::" + serial);
        };
        for (int i = 0; i < 10; i++) {
            int t = i;
            test.execute(() -> r.accept(t));
        }

        System.out.println("activeCount: " + test.getActiveCount());
        System.out.println("corePoolSize: " + test.getCorePoolSize());
        System.out.println("completedTaskCount: " + test.getCompletedTaskCount());
        System.out.println("taskCount: " + test.getTaskCount());

        TimeUnit.SECONDS.sleep(10);
        test.execute(() -> System.out.println());

        System.out.println("activeCount: " + test.getActiveCount());
        System.out.println("corePoolSize: " + test.getCorePoolSize());
        System.out.println("completedTaskCount: " + test.getCompletedTaskCount());
        System.out.println("taskCount: " + test.getTaskCount());
        test.shutdown();
    }
}

