package pub.tbc.dev.util.web.limit;

import pub.tbc.dev.util.test.P;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tbc  by 2020/3/23
 */
public class LimitStrategyTest {
    public static int REQ_COUNT = 1000000;
    public static int THREAD_COUNT = 10;

    public void multiThreadTest() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        String user = "tbc";
        LimitStrategy strategy = new TimeCallCountLimit(10, 100L);

        CountDownLatch countDownLatch = new CountDownLatch(1000);
        AtomicInteger trigger = new AtomicInteger();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < REQ_COUNT; i++) {
            executor.execute(() -> {
                boolean result = strategy.callOn(user);
                if (result) {
                    trigger.incrementAndGet();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
//        Sleeps.seconds(1);
        P.println("耗时【{}】毫秒，共调用【{}】次，触发流控【{}】次", System.currentTimeMillis() - startTime, REQ_COUNT, trigger.get());
        executor.shutdown();
    }


    //    @Test
    public void oneThreadTest() {
        String user = "tbc";
        LimitStrategy strategy = new TimeCallCountLimit(10, 100L);

        int c = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < REQ_COUNT; i++) {
            boolean result = strategy.callOn(user);
            if (result) {
                c++;
            }
        }
        P.println("耗时【{}】毫秒，共调用【{}】次，触发流控【{}】次", System.currentTimeMillis() - startTime, REQ_COUNT, c);
    }




    public static void main(String[] args) throws InterruptedException {
        LimitStrategyTest test = new LimitStrategyTest();
        for (int i = 0; i < 1000; i++) {
            test.oneThreadTest();
        }
    }
}
