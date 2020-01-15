package pub.tbc.dev.toolkit.cache;

import java.util.function.Supplier;

/**
 * @author tbc  by 2019/12/16
 */
public interface CacheProxy<T> {

    default T get(String key) {
        if (tryGet(key) == null) {

        }
        return null;
    }

    T tryGet(String key);

    T load(Supplier<T> supplier);

    T set(String key, T data);
}
