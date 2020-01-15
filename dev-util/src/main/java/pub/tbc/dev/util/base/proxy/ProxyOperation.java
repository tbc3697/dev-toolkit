package pub.tbc.dev.util.base.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tbc on 2016/11/20 20:28.
 */
@FunctionalInterface
public interface ProxyOperation {
    /**
     *
     * @param target    被代理对象
     * @param method    被调用的方法
     * @param args      参数
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object operation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}
