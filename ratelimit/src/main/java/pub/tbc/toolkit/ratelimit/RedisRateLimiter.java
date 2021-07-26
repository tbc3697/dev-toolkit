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
        return "-- 存放上一次请求后剩余数量的KEY\n" +
                "local count_key = KEYS[1]\n" +
                "-- 存放上一次请求时间\n" +
                "local time_key = KEYS[2]\n" +
                "\n" +
                "-- 本次所需令牌数量\n" +
                "local requiredCount = ARGS[1]\n" +
                "-- 令牌产生速度，个/毫秒\n" +
                "local velocity = ARGS[2]\n" +
                "\n" +
                "-- 剩余令牌数量\n" +
                "local currentTokenCount = redis.call(\"GET\", \"KEY[1]\")\n" +
                "if (currentTokenCount == nil) {\n" +
                "    currentTokenCount = 0\n" +
                "}\n" +
                "\n" +
                "-- 新的数量\n" +
                "local newCount = 0\n" +
                "if (currentTokenCount >= requiredCount) {\n" +
                "    newCount = currentTokenCount - requiredCount;\n" +
                "}\n" +
                "\n" +
                "-- local time = 当前时间";
    }
}
