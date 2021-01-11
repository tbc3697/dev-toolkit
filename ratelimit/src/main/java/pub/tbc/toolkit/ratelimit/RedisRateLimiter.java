package pub.tbc.toolkit.ratelimit;

import com.google.common.collect.Lists;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author tbc by 2021/1/7 13:31
 */
public class RedisRateLimiter extends TokenBucketRateLimiter {

    private String countSuffix = ":count";
    private String timeSuffix = ":time";

    private final StringRedisTemplate redisTemplate;
    private final String key;

    private final RedisScript<String> REDIS_ACQUIRE_LUA = new DefaultRedisScript<>(
            ""
    );


    public RedisRateLimiter(StringRedisTemplate redisTemplate, String key) {
        this.redisTemplate = redisTemplate;
        this.key = key;
    }

    @Override
    public boolean acquire(int permits) {
        List<String> keys = Lists.newArrayList(key + countSuffix, key + timeSuffix);
        String s = redisTemplate.execute(REDIS_ACQUIRE_LUA, keys, null);
        return s.equals("");
    }

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        long timeoutMillis = unit.toMillis(timeout);
        List<String> keys = Lists.newArrayList(key);
        String s = redisTemplate.execute(REDIS_ACQUIRE_LUA, keys, timeoutMillis);
        return "".equals(s);
    }

    @Override
    public boolean canAcquire(int permits, long timeout, TimeUnit unit) {
        return false;
    }

    private String tryAcquireScript() {
        return "";
    }

    /**
     * key : count/time
     * arg : 所需数量、产生速度、
     * @return
     */
    private String lockScript() {
        return "local currentTokenCount = redis.call(\"GET\", \"KEY[1]\");" +
                "local newCount = 0;" +
                "if (currentTokenCount > ARGS[1]) {" +
                "   currentTokenCount - ARGS[1];" +
                "}" +
                "// 计算" +
                "" +
                "" +
                "";
    }
}
