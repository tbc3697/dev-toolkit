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
     *
     * @param param
     * @param containThis 本次访问是否参与计算
     */
    boolean callOn(Object param, boolean containThis);

}
