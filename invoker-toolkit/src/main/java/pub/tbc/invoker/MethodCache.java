package pub.tbc.invoker;

import java.lang.reflect.Method;

/**
 * @Author tbc by 2020/8/16 4:09 下午
 */
public interface MethodCache {

    Method getMethod(String key);

    void put(String key, Method method);
}
