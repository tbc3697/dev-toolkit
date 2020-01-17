package pub.tbc.dev.util.base;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author tbc on 2016/11/9 22:44.
 */
public enum Month {
    ONE(1, "一月", "January", "Jan."),
    TWO(2, "二月", "February", "Feb."),
    THREE(3, "三月", "March", "Mar."),
    FOUR(4, "四月", "April", "Apr."),
    FIVE(5, "五月", "May", "May."),
    SIX(6, "六月", "June", "Jun."),
    SEVEN(7, "七月", "July", "Jul."),
    EIGHT(8, "八月", "August", "Aug."),
    NINE(9, "九月", "September", "Sep."),
    TEN(10, "十月", "October", "Oct."),
    ELEVEN(11, "十一月", "November", "Nov."),
    TWELVE(12, "十二月", "December", " Dec.");

    @Getter
    private int code;
    @Getter
    private String cn;
    @Getter
    private String en;
    @Getter
    private String abbr;

    Month(int code, String cn, String en, String ad) {
        this.code = code;
        this.cn = cn;
        this.en = en;
        this.abbr = ad;
    }

    public static Enums.Month of(int code) {
        return Arrays.stream(Enums.Month.values())
                .filter(m -> m.getCode() == code)
                .findAny()
                .orElseThrow(() -> new RuntimeException("no exist month code."));
    }

    public static String getCn(int code) {
        return of(code).getCn();
    }

    public static String getEn(int code) {
        return of(code).getEn();
    }

    public static String getAbbr(int code) {
        return of(code).getAbbr();
    }

}
