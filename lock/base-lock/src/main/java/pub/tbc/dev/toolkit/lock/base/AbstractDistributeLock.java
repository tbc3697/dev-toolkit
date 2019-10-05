package pub.tbc.dev.toolkit.lock.base;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;

/**
 * 抽象的分布式锁实现，封装一些共通操作和状态
 *
 * @param <T> 具体的分布式锁实现类型
 */
@Slf4j
public abstract class AbstractDistributeLock<T extends AbstractDistributeLock> implements DistributeLock<T> {

    protected AbstractDistributeLock(String lockKey) {
        this.lockKey = lockKey;
    }

    // @formatter:off

    /**
     * 常量
     * *************************************************************/

    /** 默认的锁过期时间 */
    private final long DEFAULT_EXPIRE = 30_000;

    /** 默认间隔时间 */
    private final long DEFAULT_INTERNAL = 30;

    /** 默认时区：直接使用"GMT+8"，不用 ZoneId.systemDefault(); */
    private final ZoneId DEFAULT_ZONE = ZoneId.of("GMT+8");


    /**
     * 变量
     * *************************************************************/

    /** 分布式锁 key */
    protected String lockKey;

    /** 锁过期时间 */
    protected long expire = DEFAULT_EXPIRE;

    /** 间隔时间，在超时获取锁状态时，用于在单次尝试获取锁状态失败时休眠该时间，以释放CPU */
    protected long internal = DEFAULT_INTERNAL;

    // 注：该属性为临时状态，每次【获取锁成功/释放锁成功】都要修改
    /** 锁状态：仅能表示当前进程中的某线程获得了锁 */
    protected boolean isLocked;

    // 注：该属性为临时状态，每次【获取锁成功/释放锁成功】都要修改
    /** 锁识别标识，默认可用 */
    protected String lockValue;

    // 注：该属性为临时状态，每次【获取锁成功/释放锁成功】都要修改
    /** 持有锁的线程：为空表示当前进程中没有线程获得锁，不能表示锁状态 */
    protected Thread owner;

    // 注：该属性为临时状态，每次【获取锁成功/释放锁成功】都要修改
    /** 预计锁过期时间：非精确值，仅限参考 */
    protected long expireTime;

    // @formatter:on

    /**
     * 执行加锁动作的函数: 执行加锁动作，返回加锁结果；由子类提供，因子类可获得父类的 lockKey 等信息，因此不必带参数，Supplier就好
     */
    public abstract Supplier<Boolean> lockFunc();

    /**
     * 执行锁释放动作的函数
     */
    public abstract Supplier<Boolean> releaseFunc();

    /**
     * 执行延长锁过期时间动作的函数
     */
    public abstract Function<Long, Boolean> extendExpireFunc();

    /**
     * 生成锁识别标识
     */
    protected String createLockValue() {
        return Thread.currentThread().getName() + ":" + new Random().nextLong();
    }

    /**
     * 获得锁后执行的一些状态维护工作
     */
    protected boolean afterLock() {
        isLocked = true;
        owner = Thread.currentThread();
        expireTime = expire + System.currentTimeMillis();
        return true;
    }

    /**
     * 获得锁后执行的一些状态维护工作
     */
    protected boolean afterRelease() {
        isLocked = false;
        lockValue = null;
        owner = null;
        expireTime = -1;
        return true;
    }

    protected List<String> toList(String... ss) {
        return Arrays.stream(ss).collect(Collectors.toList());
    }

    /*************************************************************
     * 继承的方法
     *************************************************************/

    @Override
    public long expireTime() {
        return expireTime;
    }

    @Override
    public LocalDateTime expireLocalDateTime() {
        return expireLocalDateTime(DEFAULT_ZONE);
    }

    @Override
    public LocalDateTime expireLocalDateTime(ZoneId zoneId) {
        return expireTime > 0 ? ofInstant(ofEpochMilli(expireTime), zoneId) : null;
    }

    @Override
    public String lockKey() {
        return lockKey;
    }

    @Override
    public boolean isOwner() {
        return Thread.currentThread().equals(owner);
    }

    @Override
    public T lockValue(String value) {
        this.lockValue = value;
        return (T) this;
    }

    @Override
    public String lockValue() {
        return lockValue;
    }

    @Override
    public boolean isLocked() {
        return isLocked && !isExpired();
    }

    @Override
    public boolean tryLock() {
        // isLocked()方法若返回true，说明已经由当前进程内的某线程获取到了锁，反之则不一定确认锁空闲
        // 暂不支持重入
        return isLocked() ? false : (isLocked = lockFunc().get());
    }

    @Override
    public boolean tryRelease() {
        // 若不是当前线程持有锁，不支持释放锁
        if (noLock() && isNotOwner()) {
            log.error("仅支持持有锁的线程释放");
            return false;
        }
        return !(isLocked = !releaseFunc().get());
    }

    @Override
    public T expire(long expire) {
        this.expire = expire;
        return (T) this;
    }

    @Override
    public long expire() {
        return expire;
    }

    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() >= expireTime;
    }

    @Override
    public boolean extendExpire(long expire) {
        // 注：不要验证锁持有者，因为通常是某线程得到锁执行任务时，启动一个新线程监视执行状态，若接近过期时任务未完成为其延期
        return extendExpireFunc().apply(expire);
    }

    @Override
    public T interval(long millisecond) {
        this.internal = millisecond;
        return (T) this;
    }

    @Override
    public long interval() {
        return this.internal;
    }

    @Override
    public boolean tryLock(long millionSecond) throws InterruptedException {
        return tryLock(millionSecond, this.internal);
    }

    @Override
    public boolean tryLock(long millionSecond, long internal) throws InterruptedException {
        // 计算超时时间点
        long outTime = millionSecond + System.currentTimeMillis();
        // 用 do while 循环，先快速尝试一次
        do {
            if (isLocked = tryLock()) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(internal);
        } while (System.currentTimeMillis() < outTime);
        return false;
    }

}
