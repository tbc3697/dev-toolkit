package pub.tbc.dev.util.base.step;

import java.util.Optional;

/**
 * 从一堆数据提取器中顺序尝试获取数据，直接成功获取到不为空的数据或者获取不到数据返回空
 *
 * @param <I> 子类
 * @param <R> 返回类型
 * @param <S> 提取器类型
 * @Author tbc by 2020/12/7 5:29 下午
 */
public interface StepGet<I extends StepGet, R, S> {
    /**
     * 增加一个提取器
     *
     * @param o
     * @return
     */
    I add(S o);


    /**
     * 获取内容
     *
     * @return
     */
    R get();

    /**
     * 获取内容
     *
     * @return
     */
    Optional<R> getOptional();

}
