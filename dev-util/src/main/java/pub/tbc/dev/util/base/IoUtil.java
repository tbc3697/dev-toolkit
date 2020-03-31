package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author tbc on 2019-05-30 18:44
 */
@Slf4j
public class IoUtil {

    public static ByteArrayOutputStream toOut(InputStream in) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while (true) {
            try {
                if ((ch = in.read()) == -1) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            swapStream.write(ch);
        }
        return swapStream;
    }

    public static void write(OutputStream out, InputStream in) throws IOException {
        ByteArrayOutputStream byteOut = toOut(in);
        out.write(byteOut.toByteArray());
    }

//    public static String getMimeType(InputStream inputStream) {
//        MagicMatch magicMatch;
//        int bytesToRead = 65536;
//        try {
//            if (bytesToRead > inputStream.available()) {
//                bytesToRead = inputStream.available();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        inputStream.mark(bytesToRead);
//        byte[] bytes = new byte[bytesToRead];
//        try {
//            inputStream.read(bytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String mimeType = "application/octet-stream";
//        try {
//            magicMatch = Magic.getMagicMatch(bytes);
//            mimeType = magicMatch.getMimeType();
//        } catch (Exception ex) {
//            log.error("MagicMatch 未能解析文件的 mime type");
//        }
//        try {
//            inputStream.reset();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return mimeType;
//    }

    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
