package pub.tbc.dev.util.base.status;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * create by tbc on 2023/8/15
 */
public class StepInvoker<S, T> {

    private Map<Integer, BiFunction<T, InvokerResult, T>> map = new HashMap();
}
