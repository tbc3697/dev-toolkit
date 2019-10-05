package pub.tbc.dev.toolkit.lock;

import pub.tbc.dev.toolkit.lock.base.DistributeLock;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLockManager implements LockManager {

    /**
     * 锁容器
     */
    private ConcurrentHashMap<String, DistributeLock> lockMap = new ConcurrentHashMap<>();


}
