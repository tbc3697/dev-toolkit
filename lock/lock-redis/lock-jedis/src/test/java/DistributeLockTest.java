import pub.tbc.dev.toolkit.lock.LockWrapper;
import pub.tbc.dev.toolkit.lock.redis.jedis.JedisLock;
import redis.clients.jedis.JedisPool;

public class DistributeLockTest {

    private JedisLock lock;


    public void before() {
        JedisPool pool = new JedisPool("127.0.0.1", 6379);
        lock = new JedisLock("pub.tbc.dev.toolkit.lock:jedis:test:001", pool);
    }

    public void lockTest() {
        try {
            if (lock.tryLock()) {
                System.out.println(String.format(
                        "pub.tbc.dev.toolkit.lock ok. key = %s, value = %s", lock.lockKey(), lock.lockValue()
                ));
            }
        } finally {
            lock.release();
        }
    }

    public void lockSupportTest() {
        LockWrapper.ofLock(lock)
                .ok(() -> System.out.println("OK"))
                .fail(() -> System.out.println("OK"))
                .exec();
    }

    public static void main(String[] args) {
        DistributeLockTest test = new DistributeLockTest();
        test.before();
        test.lockTest();
        test.lockSupportTest();


    }


}
