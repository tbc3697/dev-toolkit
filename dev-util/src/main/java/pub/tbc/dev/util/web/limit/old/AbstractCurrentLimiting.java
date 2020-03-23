package pub.tbc.dev.util.web.limit.old;

/**
 * 抽象限流容器
 *
 * @Author tbc on 2019-07-29 13:27
 */
public abstract class AbstractCurrentLimiting<T extends AbstractCurrentLimiting> implements CurrentLimiting<T> {

    private final int DEFAULT_COUNT = 100;
    private final long DEFAULT_OUT_TIME = 10_000;

    private int count = DEFAULT_COUNT;
    private long outTime = DEFAULT_OUT_TIME;

    @Override
    public int getCount(){
        return count;
    }

    @Override
    public T setPeriod(long milliseconds) {
        this.outTime = milliseconds;
        return (T)this;
    }

    @Override
    public T setCount(int count) {
        this.count = count;
        return (T)this;
    }

    @Override
    public long getOutTime(){
        return outTime;
    }

}
