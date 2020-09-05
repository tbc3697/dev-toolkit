package pub.tbc.invoker;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author tbc by 2020/8/16 4:11 下午
 */
public class DefaultInvoker implements Invoker {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private MethodCache methodCache;


    private Method getMethod(String className, String methodName, Object... params) throws ClassNotFoundException, NoSuchMethodException {
        String methodKey = getMethodKey(className, methodName);
        Method method = methodCache.getMethod(methodKey);
        if (method == null) {
            List<Class> classes = Arrays.stream(params).map(Object::getClass).collect(Collectors.toList());
            Class<?> clazz = Class.forName("");
            method = clazz.getDeclaredMethod(methodName, classes.toArray(new Class[classes.size()]));
            Objects.requireNonNull(method, "无可调用方法");
            methodCache.put(methodKey, method);
        }
        return method;
    }

    private Object getBean(String beanName) {
        // NullPointerException
        return context.getBean(beanName);
    }

    private String getMethodKey(String className, String methodName) {
        return className + "#" + methodName;
    }

    @Override
    public Object invoke(String className, String beanName, String methodName, boolean isRetry, Object... params) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        // pre(); 先入库，必须写成功，状态为调用中
        Method method = getMethod(className, methodName, params);
        Object bean = getBean(beanName);
        Object result = method.invoke(bean, params);
        // after();  回写调用结果
        return result;
    }

    private static void ddd(Object o  ) {
        Class<?> c = o.getClass();
        System.out.println("name: " + c.getName());
    }

    public static void main(String[] args) {
        Object o = new A();
        ddd(o);
    }
    @Data
    static class A {
        private String a;
    }

}
