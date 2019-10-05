package pub.tbc.dev.util.base.limit;

import java.util.concurrent.TimeUnit;

/**
 * 限流容器接口
 *
 * @Author tbc on 2019-07-29 13:18
 */
public interface CurrentLimiting<T extends CurrentLimiting> {

    /**
     * 增加一次访问
     *
     * @param identity
     * @return
     */
    T add(String identity);

    /**
     * 设置限流周期（毫秒值）
     *
     * @param milliseconds
     * @return
     */
    T setPeriod(long milliseconds);

    /**
     * 设置限流周期（指定时间单位）
     *
     * @param time
     * @param timeUnit
     * @return
     */
    default T setPeriod(long time, TimeUnit timeUnit) {
        return setPeriod(timeUnit.toMillis(time));
    }

    /**
     * 设置限流周期内的访问次数
     *
     * @param count
     * @return
     */
    T setCount(int count);

    /**
     * 是否触发限流
     *
     * @param identity
     * @return
     */
    boolean isLimit(String identity);

    /**
     * 限流周期内的访问次数
     *
     * @return
     */
    int getCount();

    /**
     * 获取限流周期
     *
     * @return
     */
    long getOutTime();

    /**
     * 若未超限，增加访问次数，并返回true，否则返回false
     *
     * @param identity
     * @return
     */
    default boolean call(String identity) {
        if (isLimit(identity)) {
            return false;
        } else {
            add(identity);
            return true;
        }
    }
}
