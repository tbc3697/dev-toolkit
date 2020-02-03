package pub.tbc.dev.util.web;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tbc  by 2020/2/3
 */
@Data
@Accessors(chain = true)
public class R {
    private int code;
    private String msg;
    private Object result;

    public static R of(int code) {
        return new R().setCode(code);
    }

    public static R ok() {
        return new R().setCode(200);
    }

    public static R ok(Object result) {
        return new R().setResult(result);
    }

    public static R reqErr() {
        return WebStatusEnum.REQ_ERR.toR();
    }

    public static R serverErr() {
        return WebStatusEnum.SERVER_ERR.toR();
    }
}
