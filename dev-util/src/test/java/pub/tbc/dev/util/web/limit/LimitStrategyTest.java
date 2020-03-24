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
    public static int REQ_COUNT = 1000;
    public static int THREAD_COUNT = 10;
    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);


    //    @Test
    public void test() throws InterruptedException {

        String user = "tbc";
        LimitStrategy strategy = new TimeCallCountLimit(10, 100L);

        CountDownLatch countDownLatch = new CountDownLatch(1000);
        AtomicInteger trigger = new AtomicInteger();
//        String msg = "【{}】【{}】收到用户【tbc】请求，流控计算结果：{}";
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < REQ_COUNT; i++) {
//            executor.execute(() -> P.println(msg, Thread.currentThread().getName(), System.currentTimeMillis(), strategy.callOn(user)));
//            executor.execute(() -> P.println(msg, Thread.currentThread().getName(), System.currentTimeMillis(), strategy.callOn(user)));
//            for (int j = 0; j < THREAD_COUNT; j++) {
            executor.execute(() -> {
                boolean result = strategy.callOn(user);
                if (result) {
                    trigger.incrementAndGet();
                }
//                    P.println(msg, Thread.currentThread().getName(), System.currentTimeMillis(), strategy.callOn(user));
                countDownLatch.countDown();
            });
//            }
//            Sleeps.milliseconds(10);
        }
        countDownLatch.await();
        P.println("耗时【{}】毫秒，共调用【{}】次，触发流控【{}】次", System.currentTimeMillis() - startTime, REQ_COUNT, trigger.get());
//        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        LimitStrategyTest test = new LimitStrategyTest();
        for (int i = 0; i < 1000; i++) {
            test.test();
        }
        executor.shutdown();
    }
}
