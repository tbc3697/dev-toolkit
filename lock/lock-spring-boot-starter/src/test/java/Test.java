import pub.tbc.dev.toolkit.lock.base.DistributeLock;
import pub.tbc.dev.toolkit.lock.redis.jedis.JedisLock;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        DistributeLock lock = new JedisLock("lock:key", null);

        // 设置过期时间，覆盖默认过期时间：
        lock.expire(20_000L);
        // 设置自旋间隔时间，
        lock.interval(100);
        // 延长过期时间
        lock.extendExpire(10_000L);

        // 链式调用
        lock.expire(1000).interval(250).tryLock(1, TimeUnit.SECONDS);


    }
}
