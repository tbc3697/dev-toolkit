package pub.tbc.dev.util.base;

import java.util.HashMap;

/**
 * @author tbc  by 2017/3/23
 */
public class FluentHashMap<K> extends HashMap<K, Object> {

    @Override
    public FluentHashMap<K> put(K key, Object value) {
        super.put(key, value);
        return this;
    }
}
