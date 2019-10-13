package pub.tbc.dev.util.distribute;

import java.util.Collection;

public class NoVirtualConsistencyHashSelector extends ConsistencyHashSelector {


    protected NoVirtualConsistencyHashSelector(Collection<String> nodes) {
        super(nodes);
    }

    @Override
    public Collection nodes() {
        return null;
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
