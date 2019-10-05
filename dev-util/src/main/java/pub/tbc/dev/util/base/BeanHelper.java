package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tbc on 2016/11/4 12:36:09.
 */
@Slf4j
public final class BeanHelper {
    private BeanHelper() {

    }

    /**
     * 获取成员变量
     */
    public static Object getField(Object bean, String fieldName) {
        Objects.requireNonNull(bean);
        Objects.requireNonNull(fieldName);
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(bean);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[获取成员变量出错，该属性不存在]-" + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置成员变量
     */
    public static <T> T setField(T bean, String fieldName, Object value) {
        // bean和属性名不能是空的
        Objects.requireNonNull(bean, "don't process null");
        Objects.requireNonNull(fieldName, "Field name cannot be empty");

        Field field;
        try {
            // getDeclaredField 可以获取private，getField只能获取公共的
            field = bean.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 验证属性是否存在
        Objects.requireNonNull(field, "field no exist");

        // 设置操作权限，以操作私有属性
        field.setAccessible(true);
        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    /**
     * 将标准javaBean转为Map<String, Object>
     *
     * @param bean 要转换的javaBean
     * @return map
     */
    public static Map<String, Object> toMapByGetter(Object bean) {
        return toMapByGetter(bean, false);
    }


    public static Map<String, Object> toMapByGetter(Object bean, boolean copyNull) {
        return toMapByGetter(bean, copyNull, (String) null);
    }

    /**
     * 将标准javaBean转为Map<String, Object>，基于getter方法的实现
     *
     * @param bean     要转换的javaBean
     * @param copyNull 是否将值为null的属性也转到Map中
     * @return Map
     * @update 2016年11月07日 14:19:59 - 增加一个过滤列表，转换时排队指定字段
     * @update 2016年11月07日 16:37:52 - 增加对getClass()方法的自动过滤，不需要显式传过滤参数
     */
    public static Map<String, Object> toMapByGetter(Object bean, boolean copyNull, String... filterFields) {
        Set<String> filterFieldList = Arrays.stream(filterFields).collect(Collectors.toSet());
        // 自动过滤
        filterFieldList.add("getClass");
        Map<String, Object> ret = new HashMap<>();
        try {
            // getMethods: 所有公用（public）方法包括其继承类的公用方法，当然也包括它所实现接口的方法。
            // 包括公共、保护、默认（包）访问和私有方法，但【【【不包括继承的方法】】】。当然也包括它所实现接口的方法。
//            Method[] methods = bean.getClass().getDeclaredMethods();
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(bean, (Object[]) null);
                    if (null == value && !copyNull) {
                        continue;
                    }
                    // 过滤不参与转换的属性
                    if (filterFieldList.contains(method.getName())) {
                        continue;
                    }
                    ret.put(field, value);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("transform fail: {}", e.getMessage(), e);
        }
        return ret;
    }

    /**
     * 基于直接操作属性的实现
     *
     * @param bean
     * @param filterFields
     * @return
     */
    public static Map<String, Object> toMap(Object bean, String... filterFields) {
        Set<String> filterFieldList = Arrays.stream(filterFields).collect(Collectors.toSet());
        // getDeclaredFields()方法，而不是getFields()，后者获取不到私有属性
        Field[] fields = bean.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> !filterFieldList.contains(f.getName()))
                .collect(Collectors.toMap(
                        field -> field.getName(),
                        f -> {
                            f.setAccessible(true);
                            try {
                                return f.get(bean);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("获取属性值失败： " + e.getMessage(), e);
                            }
                        }
                ));
    }

    public static Map<String, Object> toMap(Object bean, boolean isStaticIgnored, String... filterFields) {
        Objects.requireNonNull(bean);
        Set<String> filterFieldList = Arrays.stream(filterFields).collect(Collectors.toSet());
        Field[] fields = bean.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> !filterFieldList.contains(f.getName()))
                .filter(field -> isStaticIgnored && Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toMap(
                        field -> field.getName(),
                        f -> {
                            f.setAccessible(true);
                            try {
                                return f.get(bean);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("获取属性值失败： " + e.getMessage(), e);
                            }
                        }
                ));
    }

    public static Map<String, Object> toMap(Object bean) {
        return toMap(bean, true);
    }

    /**
     * 获取对象的字段映射（字段名 => 字段值）- copy from huang yong
     */
    public static Map<String, Object> getFieldMap(Object obj, boolean isStaticIgnored) {
        Map<String, Object> fieldMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isStaticIgnored && Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Object fieldValue = getField(obj, fieldName);
            fieldMap.put(fieldName, fieldValue);
        }
        return fieldMap;
    }

    @Deprecated
    public static <T> T fromMap(Map<String, Object> map, T t) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(t.getClass());
        } catch (IntrospectionException e) {
            log.error("获取BeanInfo对象失败：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        Arrays.stream(propertyDescriptors)
                .filter(property -> map.containsKey(property.getName()))
                .forEach(p -> {
                    // 得到property对应的setter方法
                    Method setter = p.getWriteMethod();
                    try {
                        setter.invoke(t, map.get(p.getName()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("设置属性值失败：{}", e.getMessage());
                        throw new RuntimeException(e.getMessage(), e);
                    }
                });

        return t;

    }

    /**
     * 与上面的转换方法相比，性能较好
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> c, boolean isThrowExceptionByNull) {
        if (map == null || map.isEmpty()) {
            log.error("数据源map为空，直接返回null");
            return null;
        }
        T t;
        try {
            t = c.newInstance();
            map.forEach((k, v) -> {
                try {
                    setField(t, k, v);
                } catch (RuntimeException e) {
                    if (isThrowExceptionByNull) {
                        throw new RuntimeException("map中包含Bean中不存在的属性");
                    }
                    log.debug("exception: {}", e.getMessage());
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("创建实例失败: " + e.getMessage(), e);
        }
        return t;

    }

    public static <T> T fromMap(Map<String, Object> map, Class<T> c) {
        return fromMap(map, c, false);

    }

    /**
     * 属性是否存在
     */
    public static boolean isExistField(Object bean, String fieldName) {
        Objects.requireNonNull(bean);
        Objects.requireNonNull(fieldName);
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public static String toString(Object bean) {
        return "[" + bean.getClass().getName() + "] " + toMapByGetter(bean).toString();
    }

}
