分布式锁接口及基于 Redis 的实现

支持两种实现，

使用 Spring-data-redis 时，可使用 JedisLock 或 RedisTemplateLock，在不使用 Spring 的环境下，可使用 JedisLock；

示例：

直接使用锁：
```java
JedisPool pool = new JedisPool(p, h);
JedisLock lock = new JedisLock("lockKey", pool);
if (lock.tryLock()) {
    try {
        // do something
    } finally {
        lock.release();
    }
}
```
使用锁包装工具：
```
JedisPool pool = new JedisPool(p, h);
String lockKey = "lock:key";
DistributeLock lock = new JedisLock(lockKey, pool);
LockSupport lockSupport = LockSupport.ofLock(lock)
    .ok(()->System.out.println("OK"))
    .fail(()->System.out.println("FAIL")
    .exec();
```
设置锁过期时间及自旋间隔时间等：
```
DistributeLock lock = new JedisLock("lock:key", jedisPool);

// 设置过期时间，覆盖默认过期时间：
lock.expire(20_000L);
// 设置自旋间隔时间，
lock.interval(100);
// 延长过期时间
lock.extendExpire(10_000L);

// 链式调用
lock.expire(1000).interval(250).tryLock(1, TimeUnit.SECONDS);
```

注意，支持超时的方法都支持中断，需要自行处理中断异常