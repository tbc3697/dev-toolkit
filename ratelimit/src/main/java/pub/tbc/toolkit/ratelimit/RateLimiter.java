package pub.tbc.toolkit.ratelimit;

import java.util.concurrent.TimeUnit;

/**
 * @Author tbc by 2021/1/7 13:14
 */
public interface RateLimiter {

    boolean acquire(int permits);

    boolean tryAcquire(int permits, long timeout, TimeUnit unit);

    boolean canAcquire(int permits, long timeout, TimeUnit unit);






    default boolean tryAcquire(long timeout, TimeUnit unit) {
        return tryAcquire(1, timeout, unit);
    }


    default boolean tryAcquireOnMillis(int permits, long timeout) {
        return tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
    }

    default boolean tryAcquireOnMillis(long timeout) {
        return tryAcquireOnMillis(1, timeout);
    }


    default boolean acquire() {
        return acquire(1);
    }



}
