package pub.tbc.dev.toolkit.lock.redis.spring.boot.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class LockSpringBootAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnBean(JedisPool.class)
    public JedisLockManager jedisLockManager() {
        JedisPool pool = applicationContext.getBean(JedisPool.class);
        return new JedisLockManager(pool);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public SpringLockManager springLockManager() {
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        return new SpringLockManager(redisTemplate);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
