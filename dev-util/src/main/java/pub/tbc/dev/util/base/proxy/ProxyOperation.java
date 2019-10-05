package pub.tbc.dev.util.base.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tbc on 2016/11/20 20:28.
 */
@FunctionalInterface
public interface ProxyOperation {
    Object operation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}
