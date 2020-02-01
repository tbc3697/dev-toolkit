package pub.tbc.dev.util.base;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author tbc on 2016/9/24 14:20.
 */
public class Holder<T> {

    private static final Holder<?> EMPTY = new Holder<>();

    private T value;

    private Holder() {
        this.value = null;
    }

    public static <T> Holder<T> empty() {
        @SuppressWarnings("unchecked")
        Holder<T> t = (Holder<T>) EMPTY;
        return t;
    }

    private Holder(T value) {
        this.value = Objects.requireNonNull(value);
    }

    public static <T> Holder<T> of(T value) {
        return new Holder<>(value);
    }

    public static <T> Holder<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public Holder<T> set(T t) {
        value = t;
        return this;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return value == null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public Holder<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent())
            return this;
        else
            return predicate.test(value) ? this : empty();
    }

    public <U> Holder<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Holder.ofNullable(mapper.apply(value));
        }
    }

    public <U> Holder<U> flatMap(Function<? super T, Holder<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return Objects.requireNonNull(mapper.apply(value));
        }
    }

    public <R> Stream<R> stream() {
        if (value instanceof Collection) {
            return ((Collection) value).stream();
        }
        throw new RuntimeException("不是集合，不能转换为stream");
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public T orElseThrow() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Holder)) {
            return false;
        }

        Holder<?> other = (Holder<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    @Override
    public String toString() {
        return value != null
                ? String.format("ObjectHolder[%s]", value)
                : "ObjectHolder.empty";
    }

}
