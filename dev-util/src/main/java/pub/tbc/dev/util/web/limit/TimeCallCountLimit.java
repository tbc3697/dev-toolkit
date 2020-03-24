package pub.tbc.dev.util.web.limit;

import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.web.limit.queue.CyclicBoundedQueue;
import pub.tbc.dev.util.web.limit.queue.SimpleCyclicBoundedQueueImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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

    private CyclicBoundedQueue<Long> getQueue(Object param) {
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

    /**
     * 本次访问参与计算限流<br>
     * 注意：若请求非常频繁，一直触发限流，请求时间一直在刷新，可能导致一直无法请求成功
     */
    private boolean includeThis(CyclicBoundedQueue<Long> queue) {
        long currentTimeMillis;
        long tenBe = queue.putAndReturn(currentTimeMillis = System.currentTimeMillis()).orElse(0L);
//        long interval = currentTimeMillis - tenBe;
//        P.println("[{}]用户：{}，是否触发限流：{}， 当前时间：{}， 十次前的访问时间：{}，间隔：{}",
//        Thread.currentThread().getName(), param, (currentTimeMillis - tenBe) < time, currentTimeMillis, tenBe, interval);
        return (currentTimeMillis - tenBe) < time;
    }

    @Override
    public boolean callOn(Object param, boolean containThis) {
        CyclicBoundedQueue<Long> queue = getQueue(param);
        if (containThis) {
            return includeThis(queue);
        }

        // 本次访问不参与计算
        synchronized (queue) {
            long currentTimeMillis = System.currentTimeMillis();
            long interval = currentTimeMillis - queue.ifFullGetHead().orElse(0L);
            return interval < time ? true : queue.putAndReturn(currentTimeMillis) == null;
        }
    }

}
