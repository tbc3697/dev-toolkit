package pub.tbc.dev.toolkit.lock.base;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口<br>
 * 支持超时；
 * 支持锁过期（防死锁）；
 * 不可重入；
 *
 * @param <T> 分布式锁具体实现类型
 * @Author tbc by
 */
public interface DistributeLock<T extends DistributeLock> {

    /**
     * 获取锁状态：加锁状态 = 已锁 && 未过期，用于在进程内快速判断锁状态<br>
     * 注意：若为true, 说明当前进程内的某线程已加锁成功且未过期，但反之则不能说明未加锁
     */
    boolean isLocked();

    /**
     * 获取 lockKey
     */
    String lockKey();

    /**
     * 判断当前线程是否锁持有者
     **/
    boolean isOwner();

    default boolean isNotOwner() {
        return !isOwner();
    }

    // @formatter:off

    /** 尝试加锁 */
    boolean tryLock();

    /** 尝试加锁，以毫秒为单位设置超时时间 */
    boolean tryLock(long millionSecond) throws InterruptedException;

    /** 尝试加锁，以毫秒为单位设置超时时间，并指定间隔时间；注：该方法不会覆盖默认间隔时间 */
    boolean tryLock(long millionSecond, long internal)throws InterruptedException;

    /** 尝试释放 */
    boolean tryRelease();

    // 超时相关 ++++++++++++++++++++++++++++++++++++++++++++++

    /** 设置超时获取锁状态时的间隔时间，注意：该方法将覆盖默认间隔时间 */
    T interval(long millisecond);

    /** 查看当前锁间隔时间 */
    long interval();

    // 识别标志，防止误解锁等操作 ++++++++++++++++++++++++++++++++++++++++++++++

    /** 设置一个锁识别标识，解锁等操作时需要匹配该标识 */
    T lockValue(String value);

    /** 获取锁识别标识 */
    String lockValue();


    // 过期相关 ++++++++++++++++++++++++++++++++++++++++++++++

    /** 以毫秒为单位设置过期时间 */
    T expire(long expire);

    /** 获取过期时间（long型 Unix 时间戳 ）*/
    long expire();

    /** 延长过期时间 */
    boolean extendExpire(long expire);

    /** 是否已过期 */
    boolean isExpired();

    /** 预计过期时间 */
    long expireTime();

    /** 预计过期时间：默认 GMT+8 时区 */
    LocalDateTime expireLocalDateTime();

    /** 预计过期时间 */
    LocalDateTime expireLocalDateTime(ZoneId zoneId);

    // default method //////////////////////////

    /**
     * 是否未加锁，布尔方法逆操作，方便方法引用
     **/
    default boolean noLock() {
        return !isLocked();
    }

    /**
     * 加锁，必须成功，若需要增加过期时间或者循环次数，可以重写该方法
     */
    default void lock() {
        while (noLock()) {
            tryLock();
        }
    }

    /**
     * 锁释放，必须成功，若需要增加过期时间或者循环次数，可以重写该方法
     */
    default void release() {
        while (isLocked()) {
            tryRelease();

        }
    }

    /**
     * 尝试加锁，带超时时间
     */
    default boolean tryLock(long time, TimeUnit unit)throws InterruptedException {
        return tryLock(unit.toMillis(time));
    }

    /**
     * 尝试加锁，带超时时间，以及间隔时间
     */
    default boolean tryLock(long time, TimeUnit unit, long internal, TimeUnit internalUnit)throws InterruptedException {
        return tryLock(unit.toMillis(time), internalUnit.toMillis(internal));
    }

    /**
     * 以秒为单位设置过期时间
     */
    default T setExpireSecond(long expire) {
        return expire(expire * 1000);
    }

    /**
     * 计算剩余过期时间（毫秒值）
     */
    default long computeExpire() {
        return expire() - System.currentTimeMillis();
    }

    /**
     * 设置过期时间，指定时间单位
     */
    default T expire(long expire, TimeUnit unit) {
        return expire(unit.toMillis(expire));
    }


}
