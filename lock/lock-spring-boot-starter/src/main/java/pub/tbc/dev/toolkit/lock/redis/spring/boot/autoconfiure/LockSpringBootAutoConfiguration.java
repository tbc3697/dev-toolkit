package pub.tbc.dev.toolkit.lock.redis.spring.boot.autoconfiure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import pub.tbc.dev.toolkit.lock.redis.jedis.JedisLockManager;
import pub.tbc.dev.toolkit.lock.redis.spring.SpringLockManager;
import redis.clients.jedis.JedisPool;

/**
 * @author tbc  by 2020/3/9
 */
@Configuration
public class LockSpringBootAutoConfiguration {

    @Bean
    @ConditionalOnBean(JedisPool.class)
    public JedisLockManager jedisLockManager(JedisPool pool) {
        return new JedisLockManager(pool);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public SpringLockManager springLockManager(RedisTemplate redisTemplate) {
        return new SpringLockManager(redisTemplate);
    }
}
