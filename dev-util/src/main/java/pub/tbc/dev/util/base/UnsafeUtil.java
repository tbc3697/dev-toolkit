package pub.tbc.dev.util.base;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by tbc on 2018/1/17.
 */
public class UnsafeUtil {
    /**
     * 通过反射获取{@link Unsafe}对象
     * @throws IllegalAccessException
     */
    public static Unsafe getUnsafe() throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        return (Unsafe) unsafeField.get(null);
    }

    public static void main(String[] args) throws IllegalAccessException {
        for (; ; ) {
            Unsafe unsafe = getUnsafe();
            long l = 1024 * 1024;
            long memoryAdd = unsafe.allocateMemory(1024 * 1024);
        }
    }
}
