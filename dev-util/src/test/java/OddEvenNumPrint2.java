import java.util.concurrent.CountDownLatch;

/**
 * @author tbc by 2021/7/27 19:45
 */
public class OddEvenNumPrint2 {
    private volatile long I = 0;

    public static void main(String[] args) {
        // test2();
        new OddEvenNumPrint2().test1();
    }

    private void test1() {
        new Thread(() -> {
            while (true) {
                System.out.println("******");
                // StoreLoad
                long ii = I;
                // LoadStore/LoadLoad
                if (ii > 10) {
                    break;
                } else if (ii % 2 == 0) {
                    System.out.println(Thread.currentThread().getName() + ": " + ii);
                    // LoadStore/LoadLoad
                    I++;
                    // StoreLoad
                }
            }
        }, "偶").start();

        new Thread(() -> {
            while (true) {
                System.out.println("######");
                // StoreLoad
                long ii = I;
                // LoadStore/LoadLoad
                if (ii > 10) {
                    break;
                } else if (ii % 2 == 1) {
                    System.out.println(Thread.currentThread().getName() + ": " + ii);
                    // LoadStore/LoadLoad
                    I++;
                    // StoreLoad
                }
            }
        }, "奇").start();
    }

    private static void test2() {
        OddEvenNumPrint2 print2 = new OddEvenNumPrint2();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> print2.a(0, latch), "偶").start();
        new Thread(() -> print2.a(1, latch), "奇").start();
    }

    private void a(int v, CountDownLatch latch) {
        while (true) {
            // StoreLoad
            long ii = I;
            // LoadStore/LoadLoad
            if (ii > 100) {
                break;
            } else if (ii % 2 == v) {
                System.out.println(Thread.currentThread().getName() + ": " + ii);
                // LoadStore/LoadLoad
                I++;
                // StoreLoad
            }
        }
    }
}
