package pub.tbc.dev.util.base;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tbc on 2016/8/25 10:14:14.
 */
public final class MapBuilder<K, V> {

    private final List<Map.Entry<K, V>> tempStore = new ArrayList<>();

    public static MapBuilder<String, Object> builder() {
        return new MapBuilder<>();
    }

    public MapBuilder<K, V> put(K k, V v) {
        tempStore.add(new AbstractMap.SimpleEntry(k, v));
        return this;
    }

    public HashMap<K, V> toHashMap() {
        if (EmptyUtil.isEmpty(tempStore)) {
            return new HashMap<>();
        }
        final HashMap<K, V> hashMap = new HashMap(tempStore.size());
        tempStore.forEach(entry -> hashMap.put(entry.getKey(), entry.getValue()));
        return hashMap;
    }

    public LinkedHashMap<K, V> toLinkedHashMap() {
        return new LinkedHashMap<>(toHashMap());
    }

    public TreeMap<K, V> toTreeMap() {
        return new TreeMap<>(toHashMap());
    }

    public TreeMap<K, V> toTreeMap(Comparator<? super K> comparator) {
        TreeMap<K, V> treeMap = new TreeMap<>(comparator);
        treeMap.putAll(toHashMap());
        return treeMap;
    }

    public ConcurrentHashMap<K, V> toConcurrentMap() {
        ConcurrentHashMap<K, V> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.putAll(toHashMap());
        return concurrentHashMap;
    }

    public HashMap<K, V> build() {
        return toHashMap();
    }

    public static void main(String[] args) {
        Map<String, String> map = new MapBuilder<String, String>()
                .put("k1", "v1")
                .put("k3", "v3")
                .put("k2", "v2")
                .toTreeMap();
        String s = map.get("");
        System.out.println(map);
    }
}
