package pub.tbc.dev.util.base;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;

/**
 * 封装一些 BigDecimal 的工具
 *
 * @author tbc  by 2018/8/28
 */
public class BigDecimalWrapper {

    private final int DEFAULT_ROUND = BigDecimal.ROUND_HALF_EVEN;

    private BigDecimal value;

    private int roundingMode = DEFAULT_ROUND;

    private BigDecimalWrapper(BigDecimal value) {
        this.value = value;
    }

    public static BigDecimalWrapper of(String str) {
        if (str == null || str.isEmpty()){
            throw new NullPointerException("无法从 null 生成 BigDecimalPlus 实例");
        }
        return of(new BigDecimal(str));
    }

    public static BigDecimalWrapper of(BigDecimal value) {
        if (value == null) {
            throw new NullPointerException("value值不能为空");
        }
        return new BigDecimalWrapper(value);
    }

    public static BigDecimalWrapper of(Optional<BigDecimal> value) {
        return new BigDecimalWrapper(value.orElse(BigDecimal.ZERO));
    }

    /**
     * default value = 0
     *
     * @param value
     * @return
     */
    public static BigDecimalWrapper ofNullable(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return new BigDecimalWrapper(value);
    }

    public BigDecimalWrapper setRoundingMode(int roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }

    //  ↓↓↓↓  compare method  ↓↓↓↓
    ///////////////////////////////////////////////////////////////

    /**
     * (>) 大于 greater than
     */
    public boolean gt(BigDecimal target) {
        return value.compareTo(target) > 0;
    }

    /**
     * (>) 大于 greater than
     */
    public boolean gt(String target) {
        return gt(new BigDecimal(target));
    }

    /**
     * (>=) 大于等于 greater than or equal to - $gte
     */
    public boolean gte(BigDecimal target) {
        return value.compareTo(target) >= 0;
    }

    /**
     * (>=) 大于等于 greater than or equal to - $gte
     */
    public boolean gte(String target) {
        return gte(new BigDecimal(target));
    }

    /**
     * (<) 小于 less than - $lt
     */
    public boolean lt(BigDecimal target) {
        return value.compareTo(target) < 0;
    }

    /**
     * (<) 小于 less than - $lt
     */
    public boolean lt(String target) {
        return lt(new BigDecimal(target));
    }

    /**
     * (<) 小于等于 less than or equal to - $lte
     */
    public boolean lte(BigDecimal target) {
        return value.compareTo(target) <= 0;
    }

    /**
     * (<) 小于等于 less than or equal to - $lte
     */
    public boolean lte(String target) {
        return lte(new BigDecimal(target));
    }

    public boolean eq(BigDecimal data) {
        return value.compareTo(data) == 0;
    }

    public boolean eq(String data) {
        return eq(new BigDecimal(data));
    }

    //  ↓↓↓↓  compute method  ↓↓↓↓
    ///////////////////////////////////////////////////////////////////////////////////////////

    public BigDecimalWrapper reduce(BinaryOperator<BigDecimal> accumulator, BigDecimal... bs) {
        value = Arrays.stream(bs).reduce(value, accumulator);
        return this;
    }

    public BigDecimalWrapper add(BigDecimal... bs) {
        return reduce(BigDecimal::add, bs);
    }

    public BigDecimalWrapper subtract(BigDecimal... bs) {
        return reduce(BigDecimal::subtract, bs);
    }

    public BigDecimalWrapper multiply(BigDecimal... bs) {
        return reduce(BigDecimal::multiply, bs);
    }

    public BigDecimalWrapper divide(BigDecimal... bs) {
        return divide(8, roundingMode, bs);
    }

    public BigDecimalWrapper divide(int scale, int round, BigDecimal... bs) {
        return reduce((b, a) -> b.divide(a, scale, round), bs);
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[>>" + value.toString() + "<<]";
    }

    public static void main(String[] args) {
        BigDecimal s = new BigDecimal("100");
        System.out.println(of(s).add(new BigDecimal("1"), new BigDecimal("2")));
        System.out.println(of(s));

        boolean isEq = BigDecimalWrapper.of("101").eq(s);
    }
}
