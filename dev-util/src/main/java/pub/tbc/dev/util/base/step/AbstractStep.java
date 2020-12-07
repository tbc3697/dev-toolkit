package pub.tbc.dev.util.base.step;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author tbc by 2020/12/7 5:34 下午
 */
public abstract class AbstractStep<I extends AbstractStep, R, P> implements StepGet<I, R, P> {
    private List<P> steps =  new ArrayList<>();

    @Override
    public I add(P p) {
        steps.add(p);
        return (I) this;
    }

    @Override
    public R get() {
        return getOptional().orElse(null);
    }

    @Override
    public Optional<R> getOptional() {
        R value;
        for (P step : steps) {
            if ((value = convert(step)) == null) {
                continue;
            }
            return Optional.of(value);
        }
        return Optional.empty();
    }

    abstract R convert(P p);
}
