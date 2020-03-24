package pub.tbc.dev.util.web.limit;

/**
 * 限制策略接口
 *
 * @author tbc  by 2020/3/23
 */
@FunctionalInterface
public interface LimitStrategy {

    /**
     * 访问方法，增加访问次数，返回是否超限
     */
    default boolean callOn(Object param) {
        return callOn(param, false);
    }

    /**
     * 访问方法，增加访问次数，返回是否超限
     * 注意：本次请求参与计算时，若请求非常频繁，请求时间一直在刷新，可能导致一直无法请求成功
     *
     * @param param
     * @param containThis 本次访问是否参与计算
     */
    boolean callOn(Object param, boolean containThis);

}
