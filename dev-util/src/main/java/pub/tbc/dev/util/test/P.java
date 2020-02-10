package pub.tbc.dev.util.test;

/**
 * 控制台日志输出辅助类（测试调试用，禁止生产环境使用）
 *
 * @author tbc  by 2020/2/10
 */
public final class P {
    /** 占位符 */
    public static final String PLACEHOLDER = "\\{}";
    /** 默认调用栈数组开始打印的下标 */
    public static final int DEFAULT_STACK_START_INDEX = 2;

    public static StackTraceElement[] stackTraceElements() {
        return Thread.currentThread().getStackTrace();
    }

    /**
     * 格式化日志输出
     */
    public static void print(String msg, Object... params) {
        for (Object param : params) {
            msg = msg.replaceFirst(PLACEHOLDER, param.toString());
        }
        System.out.println(msg);
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
