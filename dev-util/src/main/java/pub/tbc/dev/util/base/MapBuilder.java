package pub.tbc.dev.util.base;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tbc on 2016/8/25 10:14:14.
 */
public final class MapBuilder<K, V> {

    private final HashMap<K, V> store = new HashMap<>();

    public static MapBuilder<String, Object> builder() {
        return new MapBuilder<>();
    }

    public MapBuilder<K, V> put(K k, V v) {
        store.put(k, v);
        return this;
    }

    public HashMap<K, V> toHashMap() {
        return store;
    }

    public LinkedHashMap<K, V> toLinkedHashMap() {
        return new LinkedHashMap<>(store);
    }

    public TreeMap<K, V> toTreeMap() {
        return new TreeMap<>(store);
    }

    public TreeMap<K, V> toTreeMap(Comparator<? super K> comparator) {
        TreeMap<K, V> treeMap = new TreeMap<>(comparator);
        treeMap.putAll(store);
        return treeMap;
    }

    public ConcurrentHashMap<K, V> toConcurrentMap() {
        return new ConcurrentHashMap<>(store);
    }

    public HashMap<K, V> build() {
        return store;
    }

}
