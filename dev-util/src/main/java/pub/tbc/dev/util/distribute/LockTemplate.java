package pub.tbc.dev.util.distribute;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * java.util.concurrent.locks.Lock 使用模板
 *
 * @author tbc by 2021/7/26 10:50
 */
@Slf4j
public class LockTemplate {

    public static <R> R lockRun(Lock lock, Supplier<R> runner) {
        return lockRun(lock, runner, null);
    }

    public static <R> R lockRun(Lock lock, Supplier<R> runner, Runnable afterRunner) {
        lock.lock();
        try {
            return runner.get();
        } finally {
            if (afterRunner != null) {
                afterRunner.run();
            }
            lock.unlock();
        }
    }

    public static <R> R lockRun(Lock lock, long time, TimeUnit timeUnit, Supplier<R> runner) {
        return lockRun(lock, time, timeUnit, runner, null);
    }

    public static <R> R lockRun(Lock lock, long time, TimeUnit timeUnit, Supplier<R> runner, Runnable afterRunner) {
        try {
            if (lock.tryLock(time, timeUnit)) {
                try {
                    return runner.get();
                } finally {
                    if (afterRunner != null) {
                        afterRunner.run();
                    }
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            log.error("等待获取锁时发生中断");
        }
        throw new RuntimeException("获取锁失败");
    }

}
