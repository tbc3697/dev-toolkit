package pub.tbc.dev.util.web.limit;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.test.P;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * 在 time 毫秒内允许访问 count 次
 *
 * @author tbc  by 2020/3/23
 */
@Slf4j
public class TimeCallCountLimit implements LimitStrategy {

    private final long time;
    private final int count;
    // 存储用户及用户访问记录队列的映射
    private Map<Object, CyclicBoundedQueue<Long>> store = new ConcurrentHashMap<>();
    private ReentrantLock queueInitLock = new ReentrantLock();

    public TimeCallCountLimit(int count, long time) {
        this.time = time;
        this.count = count;
    }


    private CyclicBoundedQueue<Long> get(Object param) {
        CyclicBoundedQueue<Long> queue = store.get(param);
        if (queue == null) {
            synchronized (param.toString().intern()) {
                if ((queue = store.get(param)) == null) {
                    store.put(param, queue = new SimpleCyclicBoundedQueueImpl<>(count));
                }
            }
        }
        return queue;
    }

    private boolean containThis(CyclicBoundedQueue<Long> queue) {
        long currentTimeMillis;
        long tenBe = queue.put(currentTimeMillis = System.currentTimeMillis()).orElse(0L);
//        long interval = currentTimeMillis - tenBe;
//        P.println("[{}]用户：{}，是否触发限流：{}， 当前时间：{}， 十次前的访问时间：{}，间隔：{}",
//        Thread.currentThread().getName(), param, (currentTimeMillis - tenBe) < time, currentTimeMillis, tenBe, interval);
        return (currentTimeMillis - tenBe) < time;
    }

    /**
     * 使用该实现有个问题：若请求非常频繁，一直触发限流，可能导致一直无法请求成功
     */
    @Override
    public boolean callOn(Object param, boolean containThis) {
        CyclicBoundedQueue<Long> queue = get(param);
        if (containThis) {
            return containThis(queue);
        }
        queue.ifTruePut(p->queue.isFull());
        // 本次访问不参与计算时,
        // todo 过于宽松，假如一百请求同时处理，同时计算interval，这时如果按条件
        long currentTimeMillis = System.currentTimeMillis();
        long interval = currentTimeMillis - queue.ifFullGetHead().orElse(0L);
        return interval < time ? true : queue.put(currentTimeMillis) == null;
    }

    interface CyclicBoundedQueue<E> {

        boolean isFull();

        /**
         * 加入新元素，若队列未满，返回null，否则弹出第一个再加入，返回第一个元素
         */
        Optional<E> put(E e);

        Optional<E> head();

        Optional<E> tail();

        /**
         * 若队满返回队头元素，否则空
         */
        Optional<E> ifFullGetHead();

        boolean ifTruePut(Predicate p);

    }

    /**
     * 简单的有界队列实现，基于单向链表
     */
    static class SimpleCyclicBoundedQueueImpl<E> implements CyclicBoundedQueue<E> {
        Node<E> h, t;
        // 队列容量
        final int capacity;
        // 元素数量
        AtomicInteger count = new AtomicInteger(0);

        public SimpleCyclicBoundedQueueImpl(int capacity) {
            this.capacity = capacity;
        }

        private void addTail(Node<E> node) {
            t.next = node;
            t = node;
        }

        private Node<E> updateHead() {
            Node<E> oldH = h;
            h = h.next;
            return oldH;
        }

        @Override
        public synchronized Optional<E> put(E e) {
            Node<E> newNode = new Node<>(null, e);
            E result = null;
            if (count.intValue() == 0) { // 队列为空时
                h = t = newNode;
                count.incrementAndGet();
            } else if (count.intValue() < capacity) { // 队列非空未满时
                addTail(newNode);
                count.incrementAndGet();
            } else { // 队列已满
                addTail(newNode);
                result = updateHead().value;
            }
            if (log.isDebugEnabled()) {
                P.println("[{}]入队列，[{}]出队列", e, result == null ? "XX" : result);
            }
            return Optional.ofNullable(result);
        }

        private Optional<E> getValue(Node<E> e) {
            return Optional.ofNullable(e).map(Node::value);
        }

        @Override
        public Optional<E> head() {
            return getValue(h);
        }

        @Override
        public Optional<E> tail() {
            return getValue(t);
        }

        @Override
        public Optional<E> ifFullGetHead() {
            return isFull() ? head() : Optional.empty();
        }

        @Override
        public boolean ifTruePut(Predicate p) {
            return false;
        }

        @Override
        public boolean isFull() {
            return count.intValue() == capacity;
        }

        @Data
        @Accessors(fluent = true)
        @AllArgsConstructor
        class Node<E> {
            Node<E> next;
            E value;
        }
    }
}
