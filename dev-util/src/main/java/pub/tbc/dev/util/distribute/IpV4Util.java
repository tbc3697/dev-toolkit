package pub.tbc.dev.util.distribute;

import java.util.Arrays;
import java.util.Objects;

public class IpV4Util {

    private final String IPV4_S = "\\.";

    private boolean isInt(String s) {
        int i = 0;
        try {
            return (i = Integer.parseInt(s)) >= 0 && i < 256;
        } catch (Exception e) {
            return false;
        }
    }

    private int[] parse(String ipv4){
       return Arrays.stream(ipv4.split(IPV4_S))
                .mapToInt(Integer::valueOf)
                .toArray();
    }

    public boolean isIpV4(String ip) {
        Objects.requireNonNull(ip, "要解析的ipv4地址不能为空");
        return Arrays.stream(ip.split(IPV4_S)).allMatch(this::isIpV4);
    }

    public int hash(String ipv4) {
        if (isIpV4(ipv4)) {
            int[] s = Arrays.stream(ipv4.split(IPV4_S))
                    .mapToInt(Integer::valueOf)
                    .toArray();
            s[0] = s[0] << 24;
            s[1] = s[1] << 16;
            s[2] = s[2] << 8;
            return s[0] | s[1] | s[2] | s[3];
        }
        return ipv4.hashCode();
    }
}
