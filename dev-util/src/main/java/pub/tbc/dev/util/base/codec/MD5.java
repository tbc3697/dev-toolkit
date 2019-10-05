package pub.tbc.dev.util.base.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

/**
 * @author tbc
 * @ClassName: MD5
 * @Description: TODO
 * @date 2015年11月8日 下午1:11:06
 */
//@Service("md5")
public class MD5 {
    private static Logger log = LoggerFactory.getLogger(MD5.class);

    /**
     * 16进制32位
     *
     * @param source
     * @return
     */
    public static String getMD5(byte[] source) {
        String s = null;
        // 用来将字节转换成16进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            // MD5 的计算结果是一个 128 位的长整数,用字节表示就是 16 个字节
            byte tmp[] = md.digest();

            // 每个字节用 16 进制表示的话，使用两个字符， 所以表示成16进制需要 32 个字符
            char str[] = new char[16 * 2];
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，将 MD5 的每一个字节转换成 16进制字符
                byte byte0 = tmp[i];// 取第 i 个字节
                // 取字节中高 4 位的数字转换,// >>>为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

            }
            s = new String(str);// 换后的结果转换为字符串
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return s;
    }

    /**
     * 将字符串用MD5加密后返回（16进制，32位，小写字母）
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String s = null;
        if (null != str && !"".equals(str)) {
            s = getMD5(str.getBytes());
        }
        return s;
    }

//    public static String getMD5(String... strs) {
//        if (Objs.isEmpty(strs))
//            return null;
//        final StringBuilder sb = new StringBuilder();
//        Arrays.stream(strs).forEach(sb::append);
//        return DigestUtils.md5Hex(sb.toString());
//    }


}
