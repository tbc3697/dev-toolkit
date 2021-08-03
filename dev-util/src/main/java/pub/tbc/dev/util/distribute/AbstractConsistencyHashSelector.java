package pub.tbc.dev.util.distribute;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于一致性哈希的节点选择（不带虚拟节点）
 */
@Slf4j
public abstract class AbstractConsistencyHashSelector implements NodeSelector {

    private int HASH_POINT = (1 << 32) - 1;

    protected TreeMap<Integer, String> nodes = new TreeMap<>();

    protected AbstractConsistencyHashSelector(Collection<String> nodes) {
        this.nodes.putAll(nodes.stream().collect(Collectors.toMap(this::indexOf, Function.identity())));
    }

    /**
     * 简单扰动一下
     */
    protected int hash(String key) {
        int h;
        return (h = key.hashCode()) ^ (h >>> 24) ^ (h >>> 16) ^ (h >>> 8);
    }

    /**
     * 计算下标
     */
    protected int indexOf(String key) {
        int hash = hash(key);
        return hash & HASH_POINT;
    }

}
