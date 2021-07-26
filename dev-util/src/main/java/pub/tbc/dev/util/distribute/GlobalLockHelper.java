package pub.tbc.dev.util.distribute;

/**
 * @author tbc by 2021/7/24 16:14
 */

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 进程级别的内存锁辅助类
 */
@Slf4j
public class GlobalLockHelper {
    private static long TRY_LOCK_TIME_IN_MILLS = 1000;

    private static final Map<String, LockContainer> userLockMap = new ConcurrentHashMap<>();

    public static void lockRun(String key, Runnable runner) {
        LockContainer lockContainer = getLockContainer(key);
        lockContainer.tryLock(TRY_LOCK_TIME_IN_MILLS, key);
        try {
            runner.run();
        } finally {
            lockContainer.release();
        }
    }

    public static <R> R lockRun(String key, Supplier<R> runner) {
        return lockRun(key, s -> runner.get());
    }

    public static <R> R lockRun(String key, Function<String, R> runner) {
        R result;
        LockContainer lockContainer = getLockContainer(key);
        lockContainer.tryLock(TRY_LOCK_TIME_IN_MILLS, key);
        try {
            result = runner.apply(key);
        } finally {
            lockContainer.release();
        }
        return result;
    }

    public static LockContainer getLockContainer(String key) {
        return userLockMap.computeIfAbsent(key, k -> new LockContainer());
    }

    public static ReentrantLock getLock(String key) {
        return getLockContainer(key).lock;
    }

    /**
     * 获取正在等待锁的线程数
     *
     * @param key
     * @return
     */
    public static Integer getWaitingCount(String key) {
        return getLockContainer(key).waitingCount.get();
    }

    static class LockContainer {
        AtomicInteger waitingCount;
        ReentrantLock lock;

        LockContainer() {
            waitingCount = new AtomicInteger(0);
            lock = new ReentrantLock();
        }

        void tryLock(long ms, String key) {
            log.info("lockBeforeWaitingCount {} - {}", key, waitingCount.incrementAndGet());
            try {
                if (lock.tryLock(ms, TimeUnit.MILLISECONDS)) {
                    log.info("lockAfterWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                    return;
                }
                log.info("afterLockFailWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                throw new RuntimeException("获取锁超时：");
            } catch (InterruptedException e) {
                log.info("interruptedWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                throw new RuntimeException("获取锁失败：" + e.getMessage(), e);
            }
        }

        void tryLock0(long ms, String key, BiFunction<Long, TimeUnit, Boolean> lockGetter) {
            log.info("lockBeforeWaitingCount {} - {}", key, waitingCount.getAndIncrement());
            try {
                if (lockGetter.apply(ms, TimeUnit.MILLISECONDS)) {
                    log.info("lockAfterWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                    return;
                }
                log.info("afterLockFailWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                throw new RuntimeException("获取锁超时：");
            } catch (Exception e) {
                log.info("interruptedWaitingCount {} - {}", key, waitingCount.decrementAndGet());
                throw new RuntimeException("获取锁失败：" + e.getMessage(), e);
            }
        }

        void release() {
            lock.unlock();
        }
    }

}
