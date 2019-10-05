package pub.tbc.dev.util.base;

import lombok.Setter;

/**
 * 自定义简单Optional，线程不安全<br>
 * 可用于一些内部类(lambda)操纵的数据包装
 *
 * @author tbc on 2016/9/24 14:20.
 */
public class SimpleOptional<T> {

    @Setter
    private T value;

    private SimpleOptional() {
    }

    private SimpleOptional(T t) {
        this.value = t;
    }

    private void check() {
        if (isEmpty()) {
            throw new NullPointerException("当前SimpleOptional不包含可用的值");
        }
    }

    public static SimpleOptional<?> empty() {
        return new SimpleOptional();
    }

    public static <E> SimpleOptional<E> from(E source) {
        return new SimpleOptional<>(source);
    }

    public boolean isEmpty() {
        return EmptyUtil.isEmpty(value);
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        check();
        return value;
    }

    public T get(T defaultValue) {
        return isEmpty() ? defaultValue : value;
    }

    public T getNonEmpty(T defaultValue) {
        return isEmpty() ? defaultValue : value;
    }
}
