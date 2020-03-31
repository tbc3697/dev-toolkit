package pub.tbc.dev.util.base;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * 延时初始化辅助类
 *
 * @Author tbc on 2019-06-10 14:45
 */
public class DelayInitHelper {

    /**
     * 基于AtomicBoolean做为锁的获取延时对象的方法
     *
     * @param t
     * @param lock
     * @param s
     * @param <T>
     * @return
     */
    public static <T> T getObject(T t, AtomicBoolean lock, Supplier<T> s) {
        if (t == null) {
            do {
                if (lock.compareAndSet(false, true)) {
                    try {
                        t = s.get();
                        break;
                    } finally {
                        lock.compareAndSet(false, true);
                    }
                }
            } while (true);
        }
        return t;
    }

    public static <T> T tryGetObject(T t, AtomicBoolean lock, Supplier<T> s) {
        if (t == null) {
            if (lock.compareAndSet(false, true)) {
                try {
                    t = s.get();
                } finally {
                    lock.set(false);
                }
            }
        }
        return t;
    }

    public static <T> T getObject(T t, Supplier<T> s) {
        if (t == null) {
            synchronized (t.getClass()) {

            }
        }
        return t;
    }

}
