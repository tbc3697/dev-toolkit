package pub.tbc.dev.util.base.date;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author tbc  by 2018/6/1
 */
public enum ZoneOffsetUtil {
    UTC(ZoneOffset.UTC),
    UTC_E1(ZoneOffset.of("+1")),
    UTC_E2(ZoneOffset.of("+2")),
    UTC_E3(ZoneOffset.of("+3")),
    UTC_E4(ZoneOffset.of("+4")),
    UTC_E5(ZoneOffset.of("+5")),
    UTC_E6(ZoneOffset.of("+6")),
    UTC_E7(ZoneOffset.of("+7")),
    UTC_E8(ZoneOffset.of("+8")),
    UTC_E9(ZoneOffset.of("+9")),
    UTC_E10(ZoneOffset.of("+10")),
    UTC_E11(ZoneOffset.of("+11")),
    UTC_E12(ZoneOffset.of("+12")),
    UTC_W1(ZoneOffset.of("-1")),
    UTC_W2(ZoneOffset.of("-2")),
    UTC_W3(ZoneOffset.of("-3")),
    UTC_W4(ZoneOffset.of("-4")),
    UTC_W5(ZoneOffset.of("-5")),
    UTC_W6(ZoneOffset.of("-6")),
    UTC_W7(ZoneOffset.of("-7")),
    UTC_W8(ZoneOffset.of("-8")),
    UTC_W9(ZoneOffset.of("-9")),
    UTC_W10(ZoneOffset.of("-10")),
    UTC_W11(ZoneOffset.of("-11")),
    UTC_W12(ZoneOffset.of("-12")),
    UTC12(ZoneOffset.of("+12"));

    @Getter
    private ZoneOffset zoneOffset;

    ZoneOffsetUtil(ZoneOffset offset) {
        this.zoneOffset = offset;
    }

    public ZoneId getZoneId() {
        return ZoneId.ofOffset("UTC", zoneOffset);
    }

    public Long toTimeMillis(LocalDateTime dateTime) {
        return dateTime.toInstant(zoneOffset).toEpochMilli();
    }

    public Instant toInstant(LocalDateTime dateTime) {
        return dateTime.toInstant(zoneOffset);
    }


    public static ZoneOffsetUtil def() {
        return UTC_E8;
    }

    public static ZoneOffset defZoneOffset(){
        return UTC_E8.getZoneOffset();
    }

}
