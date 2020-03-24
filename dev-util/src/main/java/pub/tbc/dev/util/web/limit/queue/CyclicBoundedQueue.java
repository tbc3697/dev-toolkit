package pub.tbc.dev.util.web.limit.queue;

import java.util.Optional;

/**
 * @author tbc  by 2020/3/24
 */
// @formatter:off
public interface CyclicBoundedQueue<E> {

    boolean isFull();

    boolean put(E e);

    /** 加入新元素，若队列未满，返回null，否则弹出第一个再加入，返回移除的元素 */
    Optional<E> putAndReturn(E e);

    Optional<E> head();

    Optional<E> tail();

    /** 若队满返回队头元素，否则空 */
    Optional<E> ifFullGetHead();

    /** 若条件成立，执行put操作 */
    boolean ifMatchPut(Condition p, E e);

    @FunctionalInterface
    interface Condition {
        boolean test();
    }

}
