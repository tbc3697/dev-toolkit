package pub.tbc.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author tbc by 2020/8/16 4:09 下午
 */
public interface Invoker {

    /**
     * @param className  Class Name
     * @param beanName   spring Bean Name
     * @param methodName Method name
     * @param isRetry    本次调用是否属于重试调用
     * @param params     参数
     * @return
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object invoke(String className, String beanName, String methodName, boolean isRetry, Object... params) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException;

    /**
     * 默认不是重试调用
     */
    default Object invoke(String className, String beanName, String methodName, Object... params) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        return invoke(className, beanName, methodName, false, params);
    }
}
