package pub.tbc.dev.util.base.proxy;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计方法调用耗时
 *
 * @author tbc  by 2020/1/15
 */
@NoArgsConstructor
public class InvokerTimeProxy extends SurroundProxyOperation {
    long startTime;
    private Logger log = LoggerFactory.getLogger(getClass());

    public InvokerTimeProxy(Logger logger) {
        this.log = logger;
    }

    @Override
    public void before() {
        if (log != null) {
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void after() {
        String msgTemplate = "方法调用耗时：[{}] 毫秒";
        long time = System.currentTimeMillis() - startTime;
        if (log == null) {
            System.out.println(String.format(msgTemplate, time));
        }
        log.info(msgTemplate, time);
    }
}
