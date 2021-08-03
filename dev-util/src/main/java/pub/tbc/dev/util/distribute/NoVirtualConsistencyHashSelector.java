package pub.tbc.dev.util.distribute;

import java.util.Collection;
import java.util.SortedMap;

/**
 * 不支持虚拟节点的实现
 */
public class NoVirtualConsistencyHashSelector extends AbstractConsistencyHashSelector {

    protected NoVirtualConsistencyHashSelector(Collection<String> nodes) {
        super(nodes);
    }

    @Override
    public Collection nodes() {
        return super.nodes.values();
    }

    @Override
    public NodeSelector add(String t) {
        nodes.put(indexOf(t), t);
        return this;
    }

    @Override
    public NodeSelector remove(String t) {
        nodes.remove(indexOf(t));
        return this;
    }

    @Override
    public String select(Object key) {
        // 得到该key的hash值
        int indexOf = indexOf(key.toString());
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subMap = nodes.tailMap(indexOf);
        // 候选集：如果没有比给定 hash 更大的，就取第一个节点（环）
        SortedMap<Integer, String> conditionMap = subMap.isEmpty() ? nodes : subMap;
        Integer index = conditionMap.firstKey();
        return conditionMap.get(index);
    }
}
