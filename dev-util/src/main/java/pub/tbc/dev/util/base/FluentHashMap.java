package pub.tbc.dev.util.base;

import java.util.HashMap;

/**
 * @author tbc  by 2017/3/23
 */
public class FluentHashMap<K> extends HashMap<K, Object> {

    public FluentHashMap<K> fluentPut(K key, Object value) {
        put(key, value);
        return this;
    }
}
