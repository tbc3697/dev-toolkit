package pub.tbc.dev.util.distribute;

import java.util.Collection;

/**
 * 不支持虚拟节点的实现
 */
public class NoVirtualConsistencyHashSelector extends ConsistencyHashSelector {


    protected NoVirtualConsistencyHashSelector(Collection<String> nodes) {
        super(nodes);
    }

    @Override
    public Collection nodes() {
        return super.nodes.values();
    }

    @Override
    public NodeSelector add(String t) {
        return null;
    }

    @Override
    public NodeSelector remove(String t) {
        return null;
    }

    @Override
    public String select(Object key) {
        return null;
    }
}
