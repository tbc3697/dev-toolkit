import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.distribute.LockTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tbc by 2021/7/26 10:28
 */
@Slf4j
public class OddEvenNumPrint {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        OddEvenPrinter printer = new VolatileOddEvenPrinter(0, 50, 1);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(printer::print);
        executorService.execute(printer::print);
        executorService.shutdown();
    }

    private static void test2() {

    }

    interface OddEvenPrinter {
        void print();
    }

    @Slf4j
    static class LockOddEvenPrinter implements OddEvenPrinter {
        private ReentrantLock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        private Integer value;
        private Integer limit;
        private Integer step;

        public LockOddEvenPrinter(Integer value, Integer limit, Integer step) {
            this.value = value;
            this.limit = limit;
            this.step = step;
        }

        @Override
        public void print() {
            lock.lock();
            LockTemplate.lockRun(lock, () -> {
                while (value < limit) {
                    System.out.println(String.format("线程[%s]打印数字:%d", Thread.currentThread().getName(), ++value));
                    condition.signalAll();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
                return null;
            });
        }

    }

    @Slf4j
    static class VolatileOddEvenPrinter implements OddEvenPrinter {
        private volatile int value;
        private final int step;
        private int limit;


        public VolatileOddEvenPrinter(int value, int limit, int step) {
            this.value = value;
            this.limit = limit;
            this.step = step;
        }

        @Override
        public void print() {
            while (value <= limit) {
                // volatile read
                int v = value;
                log.info("print : {}", v);
                value++;
            }
        }
    }


}
