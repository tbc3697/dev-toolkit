package pub.tbc.dev.util.base.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author tbc on 2017/6/11 09:39:47.
 */
public abstract class SurroundProxyOperation implements ProxyOperation {

    @Override
    public Object operation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    public abstract void before();

    public abstract void after();
}
