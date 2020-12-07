package pub.tbc.dev.util.limit;

/**
 * @Author tbc by 2020/10/28 5:40 下午
 */

import java.util.concurrent.TimeUnit;

/**
 * @Author tbc by 2019-12
 */
public class TimeCountRateLimitByMemory implements TimeCountRateLimiter {
    /**
     * 限制次数
     */
    private int count;
    /**
     * 限流周期，毫秒
     */
    private long cycle;
    /**
     * 限流周期单位
     */
    private TimeUnit timeUnit;

    // core 计数器

    public TimeCountRateLimitByMemory(int count, long cycle) {
        this(count, cycle, TimeUnit.MICROSECONDS);
    }

    public TimeCountRateLimitByMemory(int count, long cycle, TimeUnit timeUnit) {
        this.count = count;
        this.cycle = cycle;
        this.timeUnit = timeUnit;
    }

    @Override
    public void resetCount(int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetTime(int time) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetTimeUnit(long time, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryAcquire(String key) {
        return false;
    }

    @Override
    public boolean acquire(String key, long time) {
        return false;
    }

    @Override
    public void clear(String key) {

    }
}
