package pub.tbc.dev.util.web;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author tbc  by 2020/2/3
 */
@Getter
public enum WebStatusEnum {
    OK(200, ""),
    REQ_ERR(400, "请求错误"),
    SERVER_ERR(500, "服务器内部错误")
    ;

    private int code;
    private String msg;

    WebStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public boolean equals(int code) {
        return code == this.code;
    }

    public R toR() {
        return new R().setCode(code).setMsg(msg);
    }

    public R toR(Object result) {
        return new R().setCode(code).setMsg(msg).setResult(result);
    }

    public static WebStatusEnum indexOf(int code) {
        return Stream.of(values())
                .filter(s -> s.getCode() == code)
                .findAny()
                .orElseThrow(() -> new RuntimeException());
    }


}
