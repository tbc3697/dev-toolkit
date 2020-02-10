package pub.tbc.dev.util.base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 与空值判断有关的一些常用方法
 * Created by tbc on 2018/4/17.
 */
public class EmptyUtil {

    public static <T> T orElse(T value, Supplier<T> defaultValueSupplier) {
        return isEmpty(value) ? defaultValueSupplier.get() : value;
    }

    public static <T, R> T ifNull(R r, Supplier<T> df, Function<R, T> function) {
        return isNull(r) ? df.get() : function.apply(r);
    }

    public static <T> void ifNull(T t, Runnable runnable) {
        if (isNull(t)) {
            runnable.run();
        }
    }

    /**
     * 给定对象非空时，返回第一个函数的执行结果，否则，返回第二个函数的执行结果
     *
     * @param object 要操作的对象
     * @param tf     给定对象非空时的转换函数
     * @param ff     给定对象为空时的转换函数
     * @param <T>    原始数据类型
     * @param <R>    响应的数据类型
     * @return
     */
    public static <T, R> T ifNonNull(R object, Function<R, T> tf, Function<R, T> ff) {
        if (nonEmpty(object)) {
            return tf.apply(object);
        } else {
            return ff.apply(object);
        }
    }

    public static <T> void ifNonNull(T object, Runnable runnable) {
        if (nonNull(object)) {
            runnable.run();
        }
    }

    public static <T> void ifNonNull(T value, Consumer<T> consumer) {
        if (nonNull(value)) {
            consumer.accept(value);
        }
    }

    /**
     * 如果值为Null，返回true;<br>
     * 如果是集合类型,且size为0，返回true;<br>
     * 如果是String类型，且长度为0，返回true;<br>
     * 其它情况返回false;
     *
     * @param obj Object
     * @return boolean
     * @author tbc {2016年6月17日 下午12:52:06}
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        return obj instanceof String && ((String) obj).length() == 0;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty0(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isEmpty(Collection<?> c) {
        return isNull(c) || c.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> c) {
        return isNull(c) || c.isEmpty();
    }

    public static boolean isEmpty(Object... objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean nonEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean nonEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static boolean nonEmpty(Map<?, ?> c) {
        return !isEmpty(c);
    }

    public static boolean nonEmpty(Object... objects) {
        return !isEmpty(objects);
    }


    public static boolean nonEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return !isNull(obj);
    }


}
