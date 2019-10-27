package pub.tbc.dev.toolkit.lock.redis.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pub.tbc.dev.toolkit.lock.LockManager;
import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;

@Slf4j
@Component("templateLockManager")
public class SpringLockManager implements LockManager {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public AbstractDistributeLock lock(String key) {
        return RedisTemplateLock.of(redisTemplate, key);
    }
}
