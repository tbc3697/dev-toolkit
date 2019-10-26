package pub.tbc.dev.toolkit.lock;

import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;
import pub.tbc.dev.toolkit.lock.base.ReentrantDistributeLock;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 工厂, 锁工具管理器接口
 */
public interface LockManager {

    default String createLockKey(String... ss) {
        return Arrays.stream(ss).collect(Collectors.joining(":"));
    }

    default ReentrantDistributeLock reentrantLock(String key) {
        return ReentrantDistributeLock.of(lock(key));
    }

    default LockSupport lockSupport(String key) {
        return LockSupport.ofLock(lock(key));
    }

    default LockSupport reentrantLockSupport(String key) {
        return LockSupport.ofLock(reentrantLock(key));
    }

    AbstractDistributeLock lock(String key);

}
