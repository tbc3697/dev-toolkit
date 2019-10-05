package pub.tbc.dev.util.distribute;

import java.util.Collection;

/**
 * 分布式系统节点选择接口
 *
 * @param <T>
 */
public interface NodeSelector<T> {

    Collection<T> nodes();

    NodeSelector<T> add(T t);

    NodeSelector<T> remove(T t);

    T select(Object key);

}
