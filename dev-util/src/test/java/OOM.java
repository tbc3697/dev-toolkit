import pub.tbc.dev.util.base.Sleeps;
import pub.tbc.dev.util.base.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author tbc by 2021/9/7 17:51
 */
public class OOM {
    public static void main(String[] args) throws IllegalAccessException {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> {
            while (true) {
                System.out.println("t1");
                Sleeps.seconds(1);
            }
        });
        executor.execute(() -> {
            Map<Object, Object[]> map = new HashMap<>();
            retry:
            while (true) {
                try {
                    int size = 1;
                    System.out.println("t2");
                    Object key = new Object();
                    Object[] value = new Object[size];
                    for (int i = 0; i < size; i++) {
                        value[i] = new Object();
                    }
                    map.put(key, value);
                    TimeUnit.NANOSECONDS.sleep(10);
                } catch (OutOfMemoryError | InterruptedException error) {
                    error.printStackTrace();
                    continue retry;
                }

            }
        });


        // new Thread(() -> {
        //     while (true) {
        //         System.out.println("t1");
        //         Sleeps.seconds(1);
        //     }
        // }, "t1").start();
        // new Thread(() -> {
        //     Map<Object, Object[]> map = new HashMap<>();
        //     retry:
        //     while (true) {
        //         try {
        //             int size = 1;
        //             System.out.println("t2");
        //             Object key = new Object();
        //             Object[] value = new Object[size];
        //             for (int i = 0; i <size; i++) {
        //                 value[i] = new Object();
        //             }
        //             map.put(key, value);
        //             Sleeps.milliseconds(11);
        //         } catch (OutOfMemoryError error) {
        //             error.printStackTrace();
        //             continue retry;
        //         }
        //
        //     }
        // }, "t2").start();
        Sleeps.days(1);
    }
}
