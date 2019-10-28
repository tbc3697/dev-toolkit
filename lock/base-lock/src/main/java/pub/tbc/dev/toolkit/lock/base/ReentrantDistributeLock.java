package pub.tbc.dev.toolkit.lock.base;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 组合优于继承，基于装饰模式的分布式锁可重入实现
 *
 * @author tbc  by 2019/10/20
 */
@Slf4j
public class ReentrantDistributeLock implements DistributeLock<ReentrantDistributeLock> {

    private AbstractDistributeLock lock;

    /**
     * 锁获取次数，支持重入的关键，不用考虑线程，必须获取锁成功才能修改此变量
     */
    private volatile int acquireCount;

    public ReentrantDistributeLock(AbstractDistributeLock lock) {
        this.lock = lock;
    }

    public static ReentrantDistributeLock of(AbstractDistributeLock lock) {
        return new ReentrantDistributeLock(lock);
    }

    @Override
    public boolean isLocked() {
        return lock.isLocked();
    }

    @Override
    public String lockKey() {
        return lock.lockKey();
    }

    @Override
    public boolean isOwner() {
        return lock.isOwner();
    }

    @Override
    public boolean tryLock(long millionSecond) throws InterruptedException {
        return lock.tryLock(millionSecond);
    }

    @Override
    public boolean tryLock(long millionSecond, long internal) throws InterruptedException {
        return lock.tryLock(millionSecond, internal);
    }

    @Override
    public ReentrantDistributeLock interval(long millisecond) {
        lock.interval(millisecond);
        return this;
    }

    @Override
    public long interval() {
        return lock.interval();
    }

    @Override
    public ReentrantDistributeLock lockValue(String value) {
        lock.lockValue(value);
        return this;
    }

    @Override
    public String lockValue() {
        return lock.lockValue();
    }

    @Override
    public ReentrantDistributeLock expire(long expire) {
        lock.expire(expire);
        return this;
    }

    @Override
    public long expire() {
        return lock.expire();
    }

    @Override
    public boolean extendExpire(long expire) {
        return lock.extendExpire(expire);
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public long expireTime() {
        return lock.expireTime();
    }

    @Override
    public LocalDateTime expireLocalDateTime() {
        return lock.expireLocalDateTime();
    }

    @Override
    public LocalDateTime expireLocalDateTime(ZoneId zoneId) {
        return lock.expireLocalDateTime(zoneId);
    }


    // 自行实现，非代理方法，可重入的关键

    @Override
    public boolean tryLock() {
        boolean result;
        if (result = noLock() && lock.tryLock() || (boolean) lock.lockFunc().get()) {
            acquireCount++;
        }
        return result;
    }

    @Override
    public boolean tryRelease() {
        // 若不是当前线程持有锁，不支持释放锁
        if (noLock() && isNotOwner()) {
            log.error("仅支持持有锁的线程释放");
            return false;
        }
        if (!(lock.isLocked = (!(boolean) lock.releaseFunc().get()))) {
            return --acquireCount > 0 ? true : lock.afterRelease();
        } else {
            return false;
        }
    }
}
