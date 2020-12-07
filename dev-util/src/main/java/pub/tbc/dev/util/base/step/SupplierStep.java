package pub.tbc.dev.util.base.step;


import java.util.function.Supplier;

/**
 * @Author tbc by 2020/12/7 5:31 下午
 */
public class SupplierStep<R> extends AbstractStep<SupplierStep<R>, R, Supplier<R>> {
    public static <R> SupplierStep<R> of(Class<R> rClass) {
        return new SupplierStep<>();
    }

    @Override
    R convert(Supplier<R> supplier) {
        return supplier.get();
    }
}
