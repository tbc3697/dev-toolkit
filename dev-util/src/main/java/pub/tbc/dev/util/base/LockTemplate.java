package pub.tbc.dev.util.base;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * juc锁使用模板
 *
 * @author tbc  by 2017/6/1
 */
public class LockTemplate {

    private final int DEFAULT_INTERVAL = 100;

    private int internal = DEFAULT_INTERVAL;
    private Lock lock;
    private Runnable runnable;

    public LockTemplate(Lock lock) {
        this.lock = lock;
    }

    public static LockTemplate ofLock(Lock lock) {
        return new LockTemplate(lock);
    }

    public LockTemplate run(Runnable run) {
        this.runnable = run;
        return this;
    }

    public LockTemplate internal(int internal) {
        this.internal = internal;
        return this;
    }

    public void exec(Runnable run) {
        this.runnable = run;
        doExec();
    }

    public void exec() {
        doExec();
    }

    public boolean tryExec() {
        if (lock.tryLock()) {
            try {
                runnable.run();
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public void tryDo(long outTime) {
        tryDo(outTime, internal);
    }

    public boolean tryDo(long outTime, int interval) {
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

    private void doExec() {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        LockTemplate.ofLock(lock)
                .internal(500)
                .run(()->System.out.println("aaa"))
                .tryDo(50000);
        LockTemplate.ofLock(lock).exec(() -> System.out.println("aaa"));
    }
}
