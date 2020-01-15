package pub.tbc.dev.util.base.proxy;

import lombok.extern.slf4j.Slf4j;
import pub.tbc.dev.util.base.Sleeps;

import java.lang.reflect.Proxy;
import java.util.Objects;

import static pub.tbc.dev.util.base.EmptyUtil.nonNull;

/**
 * @author tbc on 2016/11/20 20:28.
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ProxyFactory {
    /**
     * 通过JDK提供的动态代理支持返回代理对象
     *
     * @param target    被代理的实现类
     * @param operation 封装了调用该方法时要执行的操作
     * @param <T>       返回类型
     * @return T result   代理对象
     */
    public static <T> T newJdkDynamicProxy(final Object target, final ProxyOperation operation) {
        Objects.requireNonNull(target);

        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> nonNull(operation) ? operation.operation(target, method, args) : method.invoke(target, args)
        );
    }

    /**
     * 通过CGLib提供的动态代理支持返回代理对象
     *
     * @param tClass    被代理的类型
     * @param operation 封装了操作的前置和后置方法
     * @param <T>       返回类型
     * @return result   代理对象
     */
    public static <T> T newCGLibProxy(final Class<T> tClass, final ProxyOperation operation) {

        return null;
    }

    interface TestInterface {
        void doSomething();

        void doSomething(String hi);
    }

    public static void main(String[] args) {
        TestInterface target = new TestInterface() {
            @Override
            public void doSomething() {
                System.out.println("target do something");
                Sleeps.seconds(2);
            }

            @Override
            public void doSomething(String hi) {
                System.out.println("print: " + hi);
            }
        };

        TestInterface test = newJdkDynamicProxy(target, new InvokerTimeProxy());
        test.doSomething();
        test.doSomething("hello world");

    }
}
