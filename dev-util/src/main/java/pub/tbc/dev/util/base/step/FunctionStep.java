package pub.tbc.dev.util.base.step;

import java.util.function.Function;

/**
 * @Author tbc by 2020/12/7 5:41 下午
 */
public class FunctionStep<T, R> extends AbstractStep<FunctionStep<T, R>, R, Function<T, R>> {
    private final T source;

    public FunctionStep(T source) {
        this.source = source;
    }

    @Override
    R convert(Function<T, R> function) {
        return function.apply(source);
    }
}
