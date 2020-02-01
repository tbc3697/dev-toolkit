import org.junit.Test;
import pub.tbc.dev.util.base.Locker;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tbc  by 2020/2/1
 */
public class LockerTest {

    @Test
    public void test() {
        Lock lock = new ReentrantLock();
        Locker.ofLock(lock)
                .internal(500)
                .run(()->System.out.println("aaa"))
                .tryDo(50000);
        Locker.ofLock(lock).exec(() -> System.out.println("aaa"));
    }
}
