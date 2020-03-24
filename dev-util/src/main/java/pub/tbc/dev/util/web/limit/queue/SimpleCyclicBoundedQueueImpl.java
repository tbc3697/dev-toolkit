package pub.tbc.dev.util.web.limit.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.test.P;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tbc  by 2020/3/24
 */
@Slf4j
public class SimpleCyclicBoundedQueueImpl<E> implements CyclicBoundedQueue<E> {
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
    public synchronized boolean put(E e) {
        return putAndReturn(e) != null;
    }

    @Override
    public synchronized Optional<E> putAndReturn(E e) {
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
//        if (log.isDebugEnabled()) {
//            P.println("[{}]入队列，[{}]出队列", e, result == null ? "XX" : result);
//        }
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
    public synchronized boolean ifMatchPut(Condition p, E e) {
        return p.test() ? put(e) : false;
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