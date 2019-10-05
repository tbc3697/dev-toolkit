package pub.tbc.dev.util.base;


import lombok.extern.slf4j.Slf4j;

/**
 * 强制类型转换工具
 *
 * @author tbc
 * @version 1.0 {2016年6月29日 下午5:14:55}
 */
@Slf4j
public final class CastUtil {

    private static final String MSG_PREFIX = "类型强制转换失败";

    private CastUtil() {
        throw new AssertionError("No " + getClass() + " instances for you!");
    }

    private static String msg(String source, Class sourceType, Class targetType) {
        return MSG_PREFIX + ", 原始数据：[" + source + ", ->" + sourceType.getName() + "]不能转换为目标类型：" + targetType.getName();
    }

    /**
     * 处理接住的异常
     *
     * @param source     原始数据
     * @param targetType 目标数据类型
     * @param e          异常对象
     */
    private static void exception(Object source, Class targetType, Exception e) {
        log.error(msg(source.toString(), source.getClass(), targetType));
        log.error("Exception type: {}, Exception message: {}", e.getClass().getName(), e.getMessage());
        throw new RuntimeException(e.getMessage(), e);
    }

    /**
     * 强转为String（默认值为""）
     *
     * @param obj 待转换的原始对象
     * @return 结果
     * @author tbc
     * @version 1.0 {2016年6月29日 下午5:59:31}
     */
    public static String castString(Object obj) {
        return CastUtil.castString(obj, "");
    }

    /**
     * 强转为String（提供默认值）
     *
     * @param obj          待转换的原始对象
     * @param defaultValue 默认值
     * @return result
     * @author tbc
     * @version 1.0 {2016年6月29日 下午5:59:41}
     */
    public static String castString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : String.valueOf(obj);
    }

    /**
     * 强转为double（默认值为0）
     *
     * @param obj
     * @return
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:01:00}
     */
    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    /**
     * 强转为double（提供默认值）
     *
     * @param obj          原始值
     * @param defaultValue 默认值
     * @return 结果
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:00:05}
     */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (EmptyUtil.nonEmpty(strValue)) {
                try {
                    doubleValue = Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    exception(strValue, Double.class, e);
                }
            }
        }
        return doubleValue;
    }

    /**
     * 强转long ，默认0
     *
     * @param obj source
     * @return result
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:05:23}
     */
    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /**
     * 强转long ，提供默认值
     *
     * @param obj          原始值
     * @param defaultValue 默认值
     * @return result
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:02:49}
     */
    public static long castLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (EmptyUtil.nonEmpty(strValue)) {
                try {
                    longValue = Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    exception(strValue, Long.class, e);
                }
            }
        }
        return longValue;
    }

    /**
     * 强转int ，默认0
     *
     * @param obj 原始值
     * @return result
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:05:23}
     */
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /**
     * 强转int ，提供默认值
     *
     * @param obj          原始值
     * @param defaultValue 默认值
     * @return result
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:02:49}
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (obj != null) {
            String strValue = castString(obj);
            if (EmptyUtil.nonEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    exception(strValue, Integer.class, e);
                }
            }
        }
        return intValue;
    }

    /**
     * 强转Boolean，默认false,Boolean.parseBoolean的底层实现：((name != null) && name.equalsIgnoreCase("true"))
     *
     * @param obj 要转换的原始数据
     * @return tar 转换类型后的值
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:05:23}
     */
    public static boolean castBoolean(Object obj) {
        return castBoolean(obj, false);
    }

    /**
     * 强转Boolean，提供默认值->Boolean解析，只要不是true,全都是false
     *
     * @param obj          .
     * @param defaultValue .
     * @return .
     * @author tbc
     * @version 1.0 {2016年6月29日 下午6:02:49}
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

}
