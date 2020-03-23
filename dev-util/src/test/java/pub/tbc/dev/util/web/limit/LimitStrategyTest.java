package pub.tbc.dev.util.web.limit;

import pub.tbc.dev.util.base.Sleeps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tbc  by 2020/3/23
 */
public class LimitStrategyTest {


//    @Test
    public void test() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String user = "tbc";
        LimitStrategy strategy = new TimeCallCountLimit(10, 100L);

        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                strategy.callOn(user);
            });
            Sleeps.milliseconds(10);
        }
        executor.shutdown();
    }

    public static void main(String[] args){
        new LimitStrategyTest().test();
    }
}
