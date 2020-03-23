package pub.tbc.dev.util.web.limit.old;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author tbc on 2019-07-29 14:45
 */
public class LimitQueue {

    private final int capacity;
    private int size;
    private Node first;
    private Node last;
    private AtomicBoolean lock = new AtomicBoolean(false);

    public LimitQueue(int capacity) {
        if (capacity <= 0) {
            throw new RuntimeException("无意义的容量值，capacity = " + capacity);
        }
        this.capacity = capacity;
    }

    public void addLast(Long value) {
        while (true) {
            if (lock.compareAndSet(false, true)) {
                try {
                    if (size == capacity) {
                        removeFirst();
                    } else {
                        size++;
                    }
                    Node node = new Node(value);
                    if (first == null && last == null) {
                        first = last = node;
                        return;
                    }
                    node.prev = last;
                    last.next = node;
                    last = node;
                    return;
                } finally {
                    lock.set(false);
                }
            }
        }
    }

    public Long getFirst() {
        if (first == null) {
            return 0L;
        }
        return first.value;
    }

    /**
     * 如果队列已满，返回first的值，否则返回0
     *
     * @return
     */
    public Long ifFullGet() {
        return size == capacity ? getFirst() : -1;
    }

    private void removeFirst() {
        if (first == last) {
            first = last = null;
            return;
        }
        first = first.next;
        if (first == null) {
            return;
        }
        first.prev = null;
    }

    class Node {
        private Node prev;
        private Node next;
        private long value;

        Node(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
