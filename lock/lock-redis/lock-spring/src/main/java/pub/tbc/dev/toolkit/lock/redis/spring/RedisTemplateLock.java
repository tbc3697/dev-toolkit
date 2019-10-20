package pub.tbc.dev.toolkit.lock.redis.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static pub.tbc.dev.toolkit.lock.redis.RedisLockConstant.*;

/**
 * 以 RedisTemplate 做为 redis 客户端的分布式锁实现
 *
 * @Author tbc on 2019年06月06日
 */
@Slf4j
public class RedisTemplateLock extends AbstractDistributeLock<RedisTemplateLock> {

    private RedisTemplate<String, String> redisTemplate;

    protected RedisTemplateLock(String lockKey, RedisTemplate redisTemplate) {
        super(lockKey);
        this.redisTemplate = redisTemplate;
    }

    private Supplier<Boolean> lockFunc = () -> redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expire, TimeUnit.MILLISECONDS);

    private Supplier<Boolean> releaseFunc = () -> {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_SCRIPT);
        Long result = redisTemplate.execute(redisScript, toList(lockKey), lockValue);
        if (REDIS_RELEASE_OK.equals(result)) {
            return super.afterRelease();
        } else {
            log.error("解锁失败: {}", result);
            return false;
        }
    };

    private final Function<Long, Boolean> extendExpireFunc = expire -> {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(EXTEND_EXPIRE_SCRIPT);
        Long result = redisTemplate.execute(redisScript, toList(lockKey), lockValue, expire);
        if (REDIS_RELEASE_OK.equals(result)) {
            return super.afterRelease();
        } else {
            log.error("解锁失败: {}", result);
            return false;
        }
    };

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
