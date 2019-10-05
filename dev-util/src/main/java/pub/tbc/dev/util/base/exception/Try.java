package pub.tbc.dev.util.base.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 异常包装工具类
 *
 * @Author tbc on 2018-03-31 16:15
 */
@Slf4j
@NoArgsConstructor
@SuppressWarnings("all")
public class Try<T, R> {
    private T t;
    private String msg;
    private Logger logger = log;

    public Try(T t) {
        this.t = t;
    }

    /**
     * 静态工厂方法
     **********************************************************************************************/

    public static <T, R> Try<T, R> of() {
        return new Try<>();
    }

    public static <T, R> Try<T, R> of(T t) {
        return new Try<>(t);
    }


    /**
     * Class<R>并没啥鸟用，只是泛型需要，为了告诉编译器<T>的实际类型
     */
    public static <T, R> Try<T, R> of(Class<R> c) {
        return new Try<>();
    }

    public static <T, R> Try<T, R> of(T t, Class<R> c) {
        return new Try<>(t);
    }


    /**
     * 私有异常处理方法
     **********************************************************************************************/

    private RuntimeException runtimeException(Exception e) {
        String newMsg = msg == null ? e.getMessage() : String.format("%s : %s", msg, e.getMessage());
        return new RuntimeException(newMsg, e);
    }

    private void error(Exception e) {
        String newMsg = msg == null ? e.getMessage() : String.format("%s : %s", msg, e.getMessage());
        logger.error(newMsg, e);
    }

    /**
     * Fluent方法
     **********************************************************************************************/

    public Try<T, R> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Try<T, R> log(Logger log) {
        this.logger = log;
        return this;
    }

    /**
     * 函数方法
     **********************************************************************************************/

    public R get(UncheckedSupplier<R> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    public R getNoThrow(UncheckedSupplier<R> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            error(e);
            return null;
        }
    }

    public R apply(UncheckedFunction<T, R> func) {
        try {
            return func.apply(t);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    public void accept(UncheckedConsumer<T> consumer) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }

    public boolean test(UncheckedPredicate<T> predicate) {
        try {
            return predicate.test(t);
        } catch (Exception e) {
            throw runtimeException(e);
        }
    }


    /**
     * 静态方法：非受检函数式接口转换为普通函数式接口
     **********************************************************************************************/

    public static <T> Supplier<T> supplier(UncheckedSupplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, R> Function<T, R> function(UncheckedFunction<T, R> function) {
        Objects.requireNonNull(function);
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> Consumer<T> consumer(UncheckedConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T> Predicate<T> predicate(UncheckedPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return t -> {
            try {
                return predicate.test(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    /**
     * 非受检函数式接口定义
     **********************************************************************************************/
    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public interface UncheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface UncheckedConsumer<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface UncheckedPredicate<T> {
        boolean test(T value) throws Exception;
    }
}
