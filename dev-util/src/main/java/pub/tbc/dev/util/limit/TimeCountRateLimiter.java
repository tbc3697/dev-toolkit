package pub.tbc.dev.util.limit;

import java.util.concurrent.TimeUnit;

/**
 * @Author tbc by 2019-12
 */
public interface TimeCountRateLimiter {

    /**
     * 重新初始化周期内限制次数
     *
     * @param count
     */
    void resetCount(int count);

    /**
     * 重新初始化限流周期
     *
     * @param time
     */
    void resetTime(int time);

    /**
     * 重新初始化限流周期时间单位
     *
     * @param time
     * @param timeUnit
     */
    void resetTimeUnit(long time, TimeUnit timeUnit);

    /**
     * 尝试获取授权
     *
     * @param key
     * @return
     */
    boolean tryAcquire(String key);

    /**
     * 在指定时间内多次尝试获取授权
     *
     * @param key
     * @param time
     * @return
     */
    boolean acquire(String key, long time);

    /**
     * 重置某个Key的访问计数
     *
     * @param key
     */
    void clear(String key);

}
