package pub.tbc.dev.util.base;

import lombok.Getter;

/**
 * @author tbc on 2016/11/9 22:44.
 */

public enum MonthEnum {
    ONE(1, "一月", "January", "Jan"),
    TWO(2, "二月", "February", "Feb"),
    THREE(3, "三月", "March", "Mar"),
    FOUR(4, "四月", "April", "Apr"),
    FIVE(5, "五月", "May", "May"),
    SIX(6, "六月", "June", "Jun"),
    SEVEN(7, "七月", "July", "Jul"),
    EIGHT(8, "八月", "August", "Aug"),
    NINE(9, "九月", "September", "Sep"),
    TEN(10, "十月", "October", "Oct"),
    ELEVEN(11, "十一月", "November", "Nov"),
    TWELVE(12, "十二月", "December", " Dec");

    @Getter
    private int code;
    @Getter
    private String cn;
    @Getter
    private String en;
    @Getter
    private String enSimple;

    MonthEnum(int code, String cn, String en, String enSimple) {
        this.code = code;
        this.cn = cn;
        this.en = en;
        this.enSimple = enSimple;
    }

    public static MonthEnum of(int code) {
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
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            case 10:
                return TEN;
            case 11:
                return ELEVEN;
            case 12:
                return TWELVE;
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
