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

    private RedisScript<Long> RELEASE_SCRIPT_OBJ  = new DefaultRedisScript<>(RELEASE_SCRIPT);

    private RedisTemplate<String, String> redisTemplate;

    protected RedisTemplateLock(String lockKey, RedisTemplate redisTemplate) {
        super(lockKey);
        this.redisTemplate = redisTemplate;
    }

    public static RedisTemplateLock of(String lockKey, RedisTemplate redisTemplate) {
        return new RedisTemplateLock(lockKey, redisTemplate);
    }

    public static RedisTemplateLock of(RedisTemplate redisTemplate, String lockKey) {
        return new RedisTemplateLock(lockKey, redisTemplate);
    }

    @Override
    public Supplier<Boolean> lockFunc() {
        return () -> redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public Supplier<Boolean> releaseFunc() {
        return () -> {
            Long result = redisTemplate.execute(RELEASE_SCRIPT_OBJ, toList(lockKey), lockValue);
            if (REDIS_RELEASE_OK.equals(result)) {
                return true;
            } else {
                log.error("解锁失败: {}", result);
                return false;
            }
        };
    }

    @Override
    public Function<Long, Boolean> extendExpireFunc() {
        return expire -> {
            RedisScript<Long> redisScript = new DefaultRedisScript<>(EXTEND_EXPIRE_SCRIPT);
            Long result = redisTemplate.execute(redisScript, toList(lockKey), lockValue, expire);
            if (REDIS_RELEASE_OK.equals(result)) {
                return true;
            } else {
                log.error("解锁失败: {}", result);
                return false;
            }
        };
    }
}
