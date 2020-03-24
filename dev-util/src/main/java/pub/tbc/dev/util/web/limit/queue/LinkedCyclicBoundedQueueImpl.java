package pub.tbc.dev.util.web.limit.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于单向链表的简单实现
 *
 * @author tbc  by 2020/3/24
 */
@Slf4j
public class LinkedCyclicBoundedQueueImpl<E> implements CyclicBoundedQueue<E> {
    Node<E> h, t;
    // 队列容量
    final int capacity;
    // 元素数量
    final AtomicInteger count = new AtomicInteger(0);

    public LinkedCyclicBoundedQueueImpl(int capacity) {
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

    private Optional<E> getValue(Node<E> e) {
        return Optional.ofNullable(e).map(Node::getValue);
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
        return Optional.ofNullable(result);
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


    class Node<E> {
        Node<E> next;
        @Getter
        E value;

        Node(Node<E> next, E v) {
            Objects.requireNonNull(v, "节点的值不能为空");
            this.next = next;
            this.value = v;
        }
    }
}