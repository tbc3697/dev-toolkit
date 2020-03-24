package pub.tbc.dev.util.web.limit.queue;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于数组的有界队列
 *
 * @author tbc  by 2020/3/24
 */
public class ArrayCyclicBoundedQueue<E> implements CyclicBoundedQueue<E> {
    // 队列容量
    protected final int capacity;
    // 元素数量
    protected final AtomicInteger count = new AtomicInteger(0);

    // 底层原素容器
    private Object[] elements;
    // 指向头尾结点的指针
    private int h, t;

    public ArrayCyclicBoundedQueue(int capacity) {
        this.capacity = capacity;
        elements = new Object[capacity];
    }


    @Override
    public boolean isFull() {
        return count.get() == capacity;
    }

    @Override
    public boolean put(E e) {
        return putAndReturn(e) != null;
    }

    @Override
    public synchronized Optional<E> putAndReturn(E e) {
        E old = null;
        if (count.get() == 0) {
            elements[0] = e;
            h = t = 0;
            count.incrementAndGet();
        } else if (count.get() < capacity) {
            elements[t++] = e;
            count.incrementAndGet();
        } else {
            t = h++;
            if (h >= capacity) {
                h = 0;
            }
            old = (E) elements[t];
        }
        return Optional.ofNullable(old);
    }

    @Override
    public Optional<E> head() {
        return Optional.ofNullable((E) elements[h]);
    }

    @Override
    public Optional<E> tail() {
        return Optional.ofNullable((E) elements[t]);
    }

    @Override
    public Optional<E> ifFullGetHead() {
        return Optional.ofNullable(isFull() ? (E) elements[h] : null);
    }

    @Override
    public boolean ifMatchPut(Condition p, E e) {
        throw new RuntimeException();
    }

}
