package pub.tbc.dev.toolkit.lock;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.toolkit.lock.base.DistributeLock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁操作包装工具，封装了锁的获取与释放操作，接受{@link Supplier}或者{@link Runnable}对象作为获取锁成功或失败后执行的操作
 *
 * @param <T> 获取锁后，执行完要执行的任务，返回的类型
 */
@Slf4j
@RequiredArgsConstructor(staticName = "ofLock")
public class LockWrapper<T> {

    private Supplier<T> DEFAULT_FAIL_SUPPLIER = toSupplier(() -> log.error("获取锁失败"));

    @NonNull
    private DistributeLock lock;

    // @formatter:off

    /** 成功获取锁时执行的任务 */
    private Supplier<T> okSupplier;

    /** 获取锁失败时执行的任务 */
    private Supplier<T> failSupplier;

    // @formatter:on

    private T execOk() {
        return okSupplier.get();
    }

    private T execFail() {
        return failSupplier == null ? DEFAULT_FAIL_SUPPLIER.get() : failSupplier.get();
    }

    /**
     * 对于不需要返回值的操作，将 Runnable 包装成 Supplier
     *
     * @param r
     * @return
     */
    private Supplier<T> toSupplier(Runnable r) {
        return () -> {
            r.run();
            return null;
        };
    }

    private void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(lock.interval());
        } catch (InterruptedException e) {
            log.error("发生中断，暂不支持中断操作");
        }
    }

    /**
     * 尝试获取锁，若失败则按条件判断是否需要重试
     *
     * @param condition
     * @return
     */
    private T exec(Condition condition) {
        do {
            if (lock.tryLock()) {
                try {
                    return execOk();
                } finally {
                    lock.release();
                }
            }
            sleep();
        } while (condition.test());
        return execFail();
    }

    /**
     * 设定成功获取锁时的操作
     *
     * @param runnable
     * @return
     */
    public LockWrapper ok(Runnable runnable) {
        return ok(toSupplier(runnable));
    }

    /**
     * 设定成功获取锁时的操作
     *
     * @param supplier
     * @return
     */
    public LockWrapper ok(Supplier<T> supplier) {
        this.okSupplier = supplier;
        return this;
    }

    /**
     * 设置获取锁失败时的操作
     *
     * @param runnable
     * @return
     */
    public LockWrapper fail(Runnable runnable) {
        return fail(toSupplier(runnable));
    }

    /**
     * 设置获取锁失败时的操作
     *
     * @param supplier
     * @return
     */
    public LockWrapper fail(Supplier<T> supplier) {
        failSupplier = supplier;
        return this;
    }

    public T exec() {
        if (lock.tryLock()) {
            try {
                return execOk();
            } finally {
                lock.release();
            }
        } else {
            return execFail();
        }
    }

    public T exec(int reTryCount) {
        if (reTryCount <= 0) {
            throw new RuntimeException("重试次数必须大于0");
        }
        return exec(reTryCount * 1000L);
    }

    /**
     * 限定锁等待时长的操作函数
     *
     * @param waitingMilliseconds 获取锁的等待时间（毫秒）
     * @return
     */
    // 文案2：可以用lock自带的支持超时获取锁的方法
    public T exec(long waitingMilliseconds) {
        long endTime = System.currentTimeMillis() + waitingMilliseconds;
        return exec(() -> System.currentTimeMillis() < endTime);
    }

    public T ￥exec(long waitingMilliseconds) {
        long endTime = System.currentTimeMillis() + waitingMilliseconds;
        return exec(() -> System.currentTimeMillis() < endTime);
    }

    @FunctionalInterface
    interface Condition {
        boolean test();
    }

}

