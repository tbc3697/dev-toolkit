package pub.tbc.dev.toolkit.lock;

import pub.tbc.dev.toolkit.lock.base.DistributeLock;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 锁工具管理器接口，可在应用中实现，比如spring应用可注入Jedis等仓库操作工具，以及应用名等锁key拼接工具
 */
public interface LockManager {

    default String createLockKey(String... ss) {
        return Arrays.stream(ss).collect(Collectors.joining(":"));
    }

    DistributeLock lock(String key);

//    DistributeLock pub.tbc.dev.toolkit.lock(String appName, String bizType, String bizId);

//    DistributeLock pub.tbc.dev.toolkit.lock(String sysName, String appName, String bizType, String bizId);

    /**
     * 获取锁操作工具实例
     */
    LockSupport lockSupport(String key);

//    LockSupport lockSupport(String appName, String bizType, String bizId);

//    LockSupport lockSupport(String sysName, String appName, String bizType, String bizId);


}
