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
public class BigDecimalPlus {
    private BigDecimalPlus(BigDecimal value) {
        this.value = value;
    }

    private final int DEFAULT_ROUND = BigDecimal.ROUND_HALF_EVEN;

    private BigDecimal value;

    public static BigDecimalPlus of(BigDecimal value) {
        if (value == null) {
            throw new NullPointerException("value值不能为空");
        }
        return new BigDecimalPlus(value);
    }

    public static BigDecimalPlus of(Optional<BigDecimal> value) {
        return new BigDecimalPlus(value.orElse(BigDecimal.ZERO));
    }

    public static BigDecimalPlus ofNullable(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return new BigDecimalPlus(value);
    }

    /**
     * 大于
     */
    public boolean gt(BigDecimal target) {
        return value.compareTo(target) > 0;
    }

    /**
     * 大于
     */
    public boolean gt(String target) {
        return gt(new BigDecimal(target));
    }

    /**
     * (>=) 大于等于 - $gte
     */
    public boolean gte(BigDecimal target) {
        return value.compareTo(target) >= 0;
    }

    /**
     * (>=) 大于等于 - $gte
     */
    public boolean gte(String target) {
        return gte(new BigDecimal(target));
    }

    /**
     * (<) 小于 - $lt
     */
    public boolean lt(BigDecimal target) {
        return value.compareTo(target) < 0;
    }

    /**
     * (<) 小于 - $lt
     */
    public boolean lt(String target) {
        return lt(new BigDecimal(target));
    }

    /**
     * (<) 小于等 于 - $lte
     */
    public boolean lte(BigDecimal target) {
        return value.compareTo(target) <= 0;
    }

    /**
     * (<) 小于等 于 - $lte
     */
    public boolean lte(String target) {
        return lte(new BigDecimal(target));
    }

    public BigDecimalPlus addAll(BigDecimal... bs) {
        for (BigDecimal b : bs) {
            value = value.add(b);
        }
        return this;
    }

    public BigDecimalPlus reduce(BinaryOperator<BigDecimal> accumulator, BigDecimal... bs) {
        value = Arrays.stream(bs).reduce(value, accumulator);
        return this;
    }

    public BigDecimalPlus add(BigDecimal... bs) {
        return reduce(BigDecimal::add);
    }

    public BigDecimalPlus subtract(BigDecimal... bs) {
        return reduce(BigDecimal::subtract);
    }

    public BigDecimalPlus multiply(BigDecimal... bs) {
        return reduce(BigDecimal::multiply);
    }

    public BigDecimalPlus divide(BigDecimal... bs) {
        return divide(8, DEFAULT_ROUND, bs);
    }

    public BigDecimalPlus divide(int scale, int round, BigDecimal... bs) {
        return reduce((b, a) -> b.divide(a, scale, round));
    }


}
