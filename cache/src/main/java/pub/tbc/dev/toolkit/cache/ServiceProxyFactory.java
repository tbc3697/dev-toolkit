package pub.tbc.dev.toolkit.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tbc  by 2019/12/16
 */
public class ServiceProxyFactory {

    <T> T getService(ClassLoader loader, Class<T>[] interfaces, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    <T> T getService(Class<?>[] interfaces, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, handler);
    }

    <T> T getService(Class<T> interfaceClass) {

        return null;
    }

    InvocationHandler proxyHandler() {
        return (proxy, method, args) -> null;
    }

}
