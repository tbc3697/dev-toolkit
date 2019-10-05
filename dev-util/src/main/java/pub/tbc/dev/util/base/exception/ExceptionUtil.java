package pub.tbc.dev.util.base.exception;

import java.util.function.Supplier;

/**
 * Created by tbc on 2018/4/16.
 */
public class ExceptionUtil {
    public static void noException(Runnable run) {
        try {
            run.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T noException(Supplier<T> supplier) {
        return noException(supplier, null);
    }

    public static <T> T noException(Supplier<T> supplier, String message) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (message == null || message.isEmpty()) {
                throw new RuntimeException(e);
            } else {
                throw new RuntimeException(message, e);
            }

        }
    }


}
