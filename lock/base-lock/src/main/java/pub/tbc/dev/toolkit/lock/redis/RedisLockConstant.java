package pub.tbc.dev.toolkit.lock.redis;

/**
 * 一些基于 redis 的分布式锁的常量
 *
 * @Author tbc by
 */
public abstract class RedisLockConstant {

    /**
     * 释放锁使用的 LUA 脚本
     **/
    public static final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 延长过期时间使用的 LUA 脚本
     */
    // 方案2：先获取剩余过期时间，加上要续的时间，做为新的过期时间
    public static final String EXTEND_EXPIRE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";

    /**
     * 加锁成功时 redis 返回的值
     **/
    public static final String REDIS_LOCK_OK = "OK";

    /**
     * 加锁成功时 redis 返回的值
     **/
    public static final Long REDIS_RELEASE_OK = 1L;

}
