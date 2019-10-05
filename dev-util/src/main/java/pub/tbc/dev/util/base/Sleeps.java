package pub.tbc.dev.util.base;

import java.util.concurrent.TimeUnit;

/**
 * @author tbc on 2016/12/22 18:07:36.
 */
public class Sleeps {
    /**
     * 指定休眠的时间单位和值
     *
     * @param timeUnit 时间单位
     * @param value    休眠时长
     */
    public static void sleep(TimeUnit timeUnit, int value) {
        try {
            timeUnit.sleep(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 休眠指定的毫秒数
     *
     * @param milliseconds 毫秒数
     */
    public static void sleep(int milliseconds) {
        sleep(TimeUnit.MILLISECONDS, milliseconds);
    }

    public static void milliseconds(int value) {
        sleep(TimeUnit.MILLISECONDS, value);
    }

    public static void seconds(int value) {
        sleep(TimeUnit.SECONDS, value);
    }

    public static void minutes(int value) {
        sleep(TimeUnit.MINUTES, value);
    }

    public static void hours(int value) {
        sleep(TimeUnit.HOURS, value);
    }

    public static void days(int value) {
        sleep(TimeUnit.DAYS, value);
    }


}
