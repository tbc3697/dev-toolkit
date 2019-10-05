package pub.tbc.dev.toolkit.lock.redis;

import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;

/**
 * 定义一些基于 redis 的分布式锁的共通属性，基于不同 redis 客户端的锁实现可继承此类
 *
 * @param <T>
 * @Author tbc by
 */
public abstract class AbstractRedisLock<T extends AbstractRedisLock> extends AbstractDistributeLock<T> {
    protected AbstractRedisLock(String lockKey) {
        super(lockKey);
    }

    /**
     * 释放锁使用的 LUA 脚本
     **/
    protected final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 延长过期时间使用的 LUA 脚本
     */
    // 文案2：先获取剩余过期时间，加上要续的时间，做为新的过期时间
    protected final String EXTEND_EXPIRE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";

    /**
     * 加锁成功时 redis 返回的值
     **/
    protected final String REDIS_LOCK_OK = "OK";

    /**
     * 加锁成功时 redis 返回的值
     **/
    protected final Long REDIS_RELEASE_OK = 1L;

}
