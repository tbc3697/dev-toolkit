package pub.tbc.dev.util.base.codec;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.UUID;

/**
 * @author tbc
 * @version 1.0 {2016年7月8日 下午4:45:56}
 */
@Slf4j
public class CodeHelper {

    public static String getDefaultPassword() {
        return MD5.getMD5("123456");
    }

    /**
     * 生成6位随机密码
     */
    public static String randomPass6() {
        return random(6);
    }

    /**
     * 生成8位默认密码
     *
     * @return .
     */
    public static String randomPass8() {
        return random(8);
    }

    /**
     * 生成指定长度的随机密码
     *
     * @param l:密码长度，不要太长
     * @return .
     */
    public static String random(int l) {
        return random(l, false);
    }

    public static String random(int l, boolean special) {
        StringBuilder sb = new StringBuilder();
        sb.append("a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
        sb.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
        // sb.append(",~,@,#,$,%,^,&,*,(,),_,+,|,`,.");//15
        if (special) {
            //6
            sb.append(",@,#,$,%,&,_");
        }
        sb.append(",1,2,3,4,5,6,7,8,9,0");
        sb.append(",_");

        String[] arr = sb.toString().split(",");
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < l; i++) {
            // 回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）和指定值（((不包括))）之间均匀分布的 int 值。
            b.append(arr[new Random().nextInt(arr.length)]);
        }
        return b.toString();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 干净的（整洁的）UUID-不带横杠“-”
     *
     * @return
     */
    public static String getTidyUUID() {
        return getUUID().replace("-", "");
    }



}
