package pub.tbc.dev.toolkit.lock.redis.jedis;

import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.toolkit.lock.LockManager;
import pub.tbc.dev.toolkit.lock.base.AbstractDistributeLock;
import redis.clients.jedis.JedisPool;

@Slf4j
public class JedisLockManager implements LockManager {

    private JedisPool jedisPool;

    public JedisLockManager(JedisPool pool) {
        this.jedisPool = pool;
    }

    @Override
    public AbstractDistributeLock lock(String key) {
        return JedisLock.of(jedisPool, key);
    }

}
