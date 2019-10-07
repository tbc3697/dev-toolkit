package pub.tbc.dev.toolkit.lock.base;

import lombok.extern.slf4j.Slf4j;

/**
 * 支持重入的分布式锁抽象实现
 */
@Slf4j
public abstract class AbstractReentrantDistributeLock<T extends AbstractReentrantDistributeLock> extends AbstractDistributeLock<T> {

    protected AbstractReentrantDistributeLock(String lockKey) {
        super(lockKey);
    }

    /**
     * 锁获取次数，支持重入的关键，不用考虑线程，必须获取锁成功才能修改此变量
     */
    private int acquireCount;

    @Override
    public boolean tryLock() {
        boolean result = false;
        if (noLock() && super.tryLock()) {
            result = true;
        } else if (isOwner()) {
            if (lockFunc().get()) {
                result = true;
            }
        }
        if (result) {
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
        if (!(isLocked = !releaseFunc().get())) {
            return --acquireCount > 0 ? true : super.afterRelease();
        } else {
            return false;
        }
    }


}
