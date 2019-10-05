package pub.tbc.dev.util.distribute;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.TreeSet;

/**
 * 基于一致性哈希的节点选择（不带虚拟节点）
 */
@Slf4j
public class ConsistencyHashSelector<T> implements NodeSelector<T> {

    private TreeSet<T> nodes = new TreeSet<>();

    public ConsistencyHashSelector(Collection<T> nodes) {
        this.nodes.addAll(nodes);
    }

    @Override
    public Collection<T> nodes() {
        return nodes;
    }

    @Override
    public NodeSelector<T> add(T t) {
        nodes.add(t);
        return this;
    }

    @Override
    public NodeSelector<T> remove(T t) {
        nodes.remove(t);
        return this;
    }

    @Override
    public T select(Object key) {
        int hash = key.hashCode();




        return null;
    }
}
