package pub.tbc.dev.util.base.limit;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于内存的限流容器
 *
 * @Author tbc on 2019-07-29 13:34
 */
public class MemoryCurrentLimiting extends AbstractCurrentLimiting<MemoryCurrentLimiting> {

    private HashMap<String, LimitQueue> container;
    private AtomicBoolean lock = new AtomicBoolean(false) {
        public void setFalse() {
            set(false);
        }
    };

    public MemoryCurrentLimiting() {
        this(256);
    }

    public MemoryCurrentLimiting(int initialCapacity) {
        container = new HashMap<>(initialCapacity);
    }

    public MemoryCurrentLimiting(int initialCapacity, int count, long outTime) {
        container = new HashMap<>(initialCapacity);
        setCount(count);
        setPeriod(outTime);
    }

    private LimitQueue initListAndReturn(String identity) {
        while (true) {
            if (lock.compareAndSet(false, true)) {
                try {
                    LimitQueue queue = container.get(identity);
                    if (queue == null) {
                        queue = new LimitQueue(getCount());
                        container.put(identity, queue);
                    }
                    return queue;
                } finally {
                    lock.set(false);
                }
            }
        }
    }

    private LimitQueue get(String identity) {
        LimitQueue list = container.get(identity);
        if (list == null) {
            return initListAndReturn(identity);
        }
        return list;
    }

    @Override
    public MemoryCurrentLimiting add(String identity) {
        get(identity).addLast(System.currentTimeMillis());
        return this;
    }
            
    @Override
    public boolean isLimit(String identity) {
        long value = get(identity).ifFullGet();
        return value > 0 && (System.currentTimeMillis() - value) < getOutTime();
    }


}
