package pub.tbc.dev.util.base.status;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * create by tbc on 2023/8/15
 */
public class StepInvoker<S, T extends IStepStatus<S>> {
    private Map<S, BiFunction<T, InvokerResult<T>, T>> map = new HashMap<>();

    public StepInvoker<S, T> addStatusInvoke(S status, BiFunction<T, InvokerResult<T>, T> invoker) {
        this.map.put(status, invoker);
        return this;
    }

    public InvokerResult<T> invoke(T t) {
        InvokerResult<T> result = new InvokerResult<>();

        while(true) {
            S status = t.getStepStatus();
            BiFunction<T, InvokerResult<T>, T> invoker = this.map.get(status);
            if (invoker == null) {
                result.setResult(t);
                return result;
            }

            t = invoker.apply(t, result);
        }
    }
}
