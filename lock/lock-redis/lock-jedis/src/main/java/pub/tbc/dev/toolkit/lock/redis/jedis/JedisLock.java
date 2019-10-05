package pub.tbc.dev.toolkit.lock.redis.jedis;


import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.toolkit.lock.redis.AbstractRedisLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 基于 Redis 的分布式锁实现，使用 Jedis 做为 Redis 客户端;
 * 不支持重入
 */
@Slf4j
public class JedisLock extends AbstractRedisLock<JedisLock> {

    private JedisPool jedisPool;

    private final Supplier<Boolean> lockFunc = () -> {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = lockValue == null ? lockValue = createLockValue() : lockValue;
            String result = jedis.set(lockKey, value, SetParams.setParams().ex((int) expire).nx());
            if (REDIS_LOCK_OK.equals(result)) {
                return super.afterLock();
            } else {
                log.error("加锁失败: {}", result);
                return false;
            }
        }
    };

    private final Supplier<Boolean> releaseFunc = () -> {
        try (Jedis jedis = jedisPool.getResource()) {
            Object result = jedis.eval(RELEASE_SCRIPT, toList(lockKey), toList(lockValue));
            if (REDIS_RELEASE_OK.equals(result)) {
                return super.afterRelease();
            } else {
                log.error("解锁失败: {}", result);
                return false;
            }
        }
    };

    private final Function<Long, Boolean> extendExpireFunc = expire -> {
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

    private List<String> toList(String... ss) {
        return Arrays.stream(ss).collect(Collectors.toList());
    }


    public JedisLock(String lockKey, JedisPool jedisPool) {
        super(lockKey);
        this.jedisPool = jedisPool;
    }


    @Override
    public Supplier<Boolean> lockFunc() {
        return lockFunc;
    }

    @Override
    public Supplier<Boolean> releaseFunc() {
        return releaseFunc;
    }

    @Override
    public Function<Long, Boolean> extendExpireFunc() {
        return extendExpireFunc;
    }
}
