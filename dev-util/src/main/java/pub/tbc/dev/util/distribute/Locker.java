package pub.tbc.dev.util.distribute;

import pub.tbc.dev.util.base.Sleeps;

import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * {@link java.util.concurrent.locks.Lock}使用模板
 *
 * @author tbc  by 2017/6/1
 */
public class Locker {

    private final int DEFAULT_INTERVAL = 100;

    private Lock lock;
    /**
     * 尝试间隔时间【毫秒】
     */
    private int internal = DEFAULT_INTERVAL;
    private Runnable task;

    public Locker(Lock lock) {
        this.lock = lock;
    }

    public static Locker ofLock(Lock lock) {
        return new Locker(lock);
    }

    private void doExec() {
        Objects.requireNonNull(task);
        lock.lock();
        try {
            task.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 设置获取锁成功后要执行的任务
     */
    public Locker task(Runnable run) {
        this.task = run;
        return this;
    }

    /**
     * 设置加锁失败后的重试间隔时间
     */
    public Locker internal(int internal) {
        this.internal = internal;
        return this;
    }

    /**
     * 尝试执行任务：先尝试获取锁，成功则执行任务并返回true，失败则返回false
     */
    public boolean tryExec() {
        Objects.requireNonNull(task);
        if (lock.tryLock()) {
            try {
                task.run();
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    /**
     * 在outTime时间内反复尝试获取锁并执行任务
     */
    public void tryExec(long outTime) {
        tryExec(outTime, internal);
    }

    /**
     * 指定超时时间和重试间隔时间，尝试
     *
     * @param outTime  获取锁的超时时间
     * @param interval 重试间隔时间
     */
    public boolean tryExec(long outTime, int interval) {
        long timeout = System.currentTimeMillis() + outTime;
        if (timeout > 0) {
            return tryExec();
        }
        do {
            if (tryExec()) {
                return true;
            }
            Sleeps.milliseconds(interval);
        } while (timeout < System.currentTimeMillis());
        return false;
    }

    /**
     * 执行指定任务
     */
    public void exec(Runnable run) {
        this.task = run;
        doExec();
    }

    public void exec() {
        doExec();
    }

}
