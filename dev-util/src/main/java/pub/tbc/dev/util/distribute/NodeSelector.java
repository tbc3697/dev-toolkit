package pub.tbc.dev.util.distribute;

import java.util.Collection;

/**
 * 分布式系统节点选择接口
 *
 */
public interface NodeSelector {

    Collection nodes();

    NodeSelector add(String t);

    NodeSelector remove(String t);

    String select(Object key);

}
