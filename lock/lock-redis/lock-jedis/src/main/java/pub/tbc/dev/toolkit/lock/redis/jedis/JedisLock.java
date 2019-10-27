package pub.tbc.dev.toolkit.lock.redis.jedis;


import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.function.Function;
import java.util.function.Supplier;

import static pub.tbc.dev.toolkit.lock.redis.RedisLockConstant.*;

/**
 * 基于 Redis 的分布式锁实现，使用 Jedis 做为 Redis 客户端;
 * 不支持重入
 */
@Slf4j
public class JedisLock extends AbstractDistributeLock<JedisLock> {

    private JedisPool jedisPool;

    public JedisLock(String lockKey, JedisPool jedisPool) {
        super(lockKey);
        this.jedisPool = jedisPool;
    }

    public JedisLock(JedisPool jedisPool, String lockKey) {
        super(lockKey);
        this.jedisPool = jedisPool;
    }

    public static JedisLock of(String lockKey, JedisPool jedisPool){
        return new JedisLock(lockKey, jedisPool);
    }

    public static JedisLock of(JedisPool jedisPool, String lockKey){
        return new JedisLock(lockKey, jedisPool);
    }

    @Override
    public Supplier<Boolean> lockFunc() {
        return () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String value = lockValue == null ? lockValue = createLockValue() : lockValue;
                String result = jedis.set(lockKey, value, SetParams.setParams().px((int) expire).nx());
                if (REDIS_LOCK_OK.equals(result)) {
                    return true;
                } else {
                    log.error("加锁失败: {}", result);
                    return false;
                }
            }
        };
    }

    @Override
    public Supplier<Boolean> releaseFunc() {
        return () -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Object result = jedis.eval(RELEASE_SCRIPT, toList(lockKey), toList(lockValue));
                if (REDIS_RELEASE_OK.equals(result)) {
                    return true;
                } else {
                    log.error("解锁失败: {}", result);
                    return false;
                }
            }
        };
    }

    @Override
    public Function<Long, Boolean> extendExpireFunc() {
        return expire -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Object result = jedis.eval(EXTEND_EXPIRE_SCRIPT, toList(lockKey), toList(lockValue, String.valueOf(expire)));
                if (REDIS_RELEASE_OK.equals(result)) {
                    return super.afterRelease();
                } else {
                    log.error("延长过期时间失败: {}", result);
                    return false;
                }
            }
        };
    }
}
