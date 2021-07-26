import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.distribute.GlobalLockHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tbc by 2021/7/24 16:23
 */
@Slf4j
public class LockUtilTest {
    public static void main(String[] args) {
        String[] users = {"张三"};//, "李四", "王二麻子"};
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            final int v = i;
            executorService.execute(() -> {
                String user = users[v % users.length];
                GlobalLockHelper.lockRun(user, () -> bizProcess(user));
            });
        }
        executorService.shutdown();
    }

    public static String bizProcess(String user) {
        log.info("业务处理:{} - {}", user, GlobalLockHelper.getWaitingCount(user));
        return user;
    }
}
