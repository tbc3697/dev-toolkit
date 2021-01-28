package pub.tbc.dev.util.base;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 线程池构建器，暂只支持构建普通线程池
 *
 * @Author tbc by 2019/2/27 5:21 下午
 */
@Data
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
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
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
            public Thread newThread(@NotNull Runnable r) {
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

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(core, max, k, unit, queue, t, handler);
        // Core threads must have nonzero keep alive times
        if (keepAliveTime != 0 && allowCoreThreadTimeOut) {
            threadPoolExecutor.allowCoreThreadTimeOut(true);
        }
        return threadPoolExecutor;
    }

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
