package pub.tbc.dev.util.test;

import java.util.function.Consumer;

/**
 * 控制台日志输出辅助类（测试调试用，禁止生产环境使用）
 *
 * @author tbc  by 2020/2/10
 */
public final class P {
    // @formatter:off
    /** 占位符: {} */
    public static final String PLACEHOLDER = "\\{}";
    /** 默认调用栈数组开始打印的下标 */
    public static final int DEFAULT_STACK_START_INDEX = 2;
    // @formatter:on

    private static String msg(String msg, Object... params) {
        if (params != null && params.length > 0) {
            for (Object param : params) {
                msg = msg.replaceFirst(PLACEHOLDER, param == null ? "null" : param.toString());
            }
        }
        return msg;
    }

    public static StackTraceElement[] stackTraceElements() {
        return Thread.currentThread().getStackTrace();
    }

    public static void printf(String msg, Object... params) {
        System.out.printf(msg, params);
    }

    public static void print0(String msg, Consumer<String> consumer) {
        consumer.accept(msg);
    }

    /**
     * 格式化日志输出，换行
     */
    public static void println(String msg, Object... params) {
        print0(msg(msg, params), System.out::println);
    }

    public static void println(String msg) {
        print0(msg, System.out::println);
    }

    /**
     * 格式化日志输出，不换行
     */
    public static void print(String msg, Object... params) {
        print0(msg(msg, params), System.out::print);
    }

    public static void print(String msg) {
        print0(msg, System.out::println);
    }

    /**
     * 从默认位置打印调用栈
     */
    public static void printStack() {
        printStack(DEFAULT_STACK_START_INDEX);
    }

    /**
     * 从调用栈的第start个位置（含0）开始打印调用栈
     */
    public static void printStack(int start) {
        StackTraceElement[] stackTraceElements = stackTraceElements();
        int length = stackTraceElements.length;
        for (int i = start; i < length; i++) {
            System.out.println(stackTraceElements[i]);
        }
    }

    /**
     * 打印日志及调用栈
     */
    public static void printStack(String msg, Object... params) {
        print(msg, params);
        printStack(3);
    }

}
