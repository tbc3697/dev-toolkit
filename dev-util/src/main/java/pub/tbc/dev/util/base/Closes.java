package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tbc on 2017/2/2 09:20:50.
 */
@Slf4j
public class Closes {
    private Closes() {
        throw new AssertionError("No " + getClass().getCanonicalName() + " instances for you!");
    }

    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (closeable != null) {
                try {
                    log.debug("close object : {}", closeable.getClass().getName());
                    closeable.close();
                } catch (Exception e) {
                    log.error("close to AutoCloseable failure : ", e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
