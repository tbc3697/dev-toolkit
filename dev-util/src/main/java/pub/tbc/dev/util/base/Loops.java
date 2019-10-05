package pub.tbc.dev.util.base;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 基于函数回调提供更快捷方便的循环功能
 *
 * @author tbc
 * @version 1.0 {2016年8月2日 下午6:04:14}
 */
public final class Loops {

    private Loops() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static void times(int end, Consumer<Integer> consumer) {
        upto(0, end, consumer);
    }

    public static void upto(int start, int end, Consumer<Integer> consumer) {
        step(start, end, 1, consumer);
    }

    /**
     * 从start（含）到end（不含）遍历，每次加step
     *
     * @param start    起始值
     * @param end      结束值
     * @param step     步长
     * @param consumer 消费函数
     */
    public static void step(int start, int end, int step, Consumer<Integer> consumer) {
        if (start > end) {
            end = start + end;
            start = end - start;
            end = end - start;
        }

        for (int i = start; i < end; i += step) {
            consumer.accept(i);
        }
    }

    public static double sum(Iterator<Double> numbers) {
        double sum = 0.0D;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

    public static float sumFloat(Iterator<Float> numbers) {
        float sum = 0.0f;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }


    public static long sumLong(Iterator<Long> numbers) {
        long sum = 0L;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

    public static int sumInt(Iterator<Double> numbers) {
        int sum = 0;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

}
