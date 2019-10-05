package pub.tbc.dev.util.base.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author tbc on 2018年01月17日 22:06:05
 */
public abstract class DateFormats {

    public static final String STR_YMD = "yyyy-MM-dd";
    public static final String STR_HMS = "HH:mm:ss";
    public static final String STR_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String STR_MILL = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String STR_YYYY = "yyyy";
    public static final String STR_YY = "yy";
    public static final String STR_MM = "MM";
    public static final String STR_DD = "dd";
    public static final String STR_HH = "HH";
    public static final String STR_MI = "mm";
    public static final String STR_SS = "ss";
    public static final String STR_SSS = "SSS";

    public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter HMS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter YMD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter MILL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter YY = DateTimeFormatter.ofPattern("yy");
    public static final DateTimeFormatter MM = DateTimeFormatter.ofPattern("MM");
    public static final DateTimeFormatter DD = DateTimeFormatter.ofPattern("dd");
    public static final DateTimeFormatter HH = DateTimeFormatter.ofPattern("HH");
    public static final DateTimeFormatter MI = DateTimeFormatter.ofPattern("mm");
    public static final DateTimeFormatter SS = DateTimeFormatter.ofPattern("ss");
    public static final DateTimeFormatter SSS = DateTimeFormatter.ofPattern("SSS");

    public static final String FORMAT_DEFAULT = STR_YMD_HMS;

    public static String format(long millis) {
        return format(new Date(millis));
    }

    public static String format(Date date) {
        return format(date, FORMAT_DEFAULT);
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static DateTimeFormatter getFormatter(String format) {
        return DateTimeFormatter.ofPattern(format);
    }

    public static DateTimeFormatter defaultFormatter() {
        return DateTimeFormatter.ofPattern(FORMAT_DEFAULT);
    }

    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern(STR_YMD);
    }

    public static DateTimeFormatter timeFormatter() {
        return DateTimeFormatter.ofPattern(STR_HMS);
    }


    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        System.out.println(dateTime.format(DateTimeFormatter.ofPattern(STR_YY)));

    }

}
