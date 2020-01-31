分布式锁接口及基于 Redis 的实现

支持两种实现，

使用 Spring-data-redis 时，可使用 JedisLock 或 RedisTemplateLock，在不使用 Spring 的环境下，可使用 JedisLock；

示例：

直接使用锁：
```
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
LockWrapper locker = LockWrapper.ofLock(lock)
    .ok(()->System.out.println("OK"))
    .fail(()->System.out.println("FAIL");// 可以不指定，默认获取锁失败后将打印失败日志，不执行其它动作

// 具体执行：只尝试获取一次锁，不成功就执行失败任务
locker.exec();
// 带超时时间，获取锁失败时，会在超时时间内多次重试
locker.exec(5_000);
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

***
可通过锁内置的 isXXX 等方法查看锁状态，但此类方法作用区仅限进程内，不能确切表示锁状态；

比如，isLocked()方法返回true时，可以确认锁已被获取，但反之，不能判断锁未被获取；

同样，过期时间也不精确，是得到锁后计算出来的；