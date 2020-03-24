package pub.tbc.dev.util.web.limit.queue;

import java.util.Optional;

/**
 * 有界队列：当队列满时添加新元素，移除 head 后将新元素加到队尾
 *
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
    @Deprecated
    boolean ifMatchPut(Condition p, E e);

    @Deprecated
    @FunctionalInterface
    interface Condition {
        boolean test();
    }

}
