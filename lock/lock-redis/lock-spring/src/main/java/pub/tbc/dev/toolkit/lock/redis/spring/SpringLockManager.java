package pub.tbc.dev.toolkit.lock.redis.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import pub.tbc.dev.toolkit.lock.LockManager;

@Slf4j
public class SpringLockManager implements LockManager {
    private RedisTemplate redisTemplate;

    public SpringLockManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplateLock lock(String key) {
        return RedisTemplateLock.of(redisTemplate, key);
    }
}
