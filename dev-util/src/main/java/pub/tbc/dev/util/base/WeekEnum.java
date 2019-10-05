package pub.tbc.dev.util.base;

import lombok.Getter;

/**
 * @author tbc on 2016/11/9 23:01.
 */
public enum WeekEnum {
    ONE(1, "星期一", "Monday", "Mon"),
    TWO(2, "星期二", "Tuesday", "Tue"),
    THREE(3, "星期三", "Wednesday", "Wed"),
    FOUR(4, "星期四", "Thursday", "Thurs"),
    FIVE(5, "星期五", "Friday", "fri"),
    SIX(6, "星期六", "Saturday", "Sat"),
    SEVEN(7, "星期日", "Sunday", "Sum");

    @Getter
    private int code;
    @Getter
    private String cn;
    @Getter
    private String en;
    @Getter
    private String enSimple;

    WeekEnum(int code, String cn, String en, String enSimple) {
        this.code = code;
        this.cn = cn;
        this.en = en;
        this.enSimple = enSimple;
    }

    public static WeekEnum of(int code) {
        switch (code) {
            case 1:
                return ONE;
            case 2:
                return TWO;
            case 3:
                return THREE;
            case 4:
                return FOUR;
            case 5:
                return FIVE;
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            default:
                throw new AssertionError();
        }
    }

    public static String getCn(int code) {
        return of(code).getCn();
    }

    public static String getEn(int code) {
        return of(code).getEn();
    }

    public static String getEnSimple(int code) {
        return of(code).getEnSimple();
    }

}
