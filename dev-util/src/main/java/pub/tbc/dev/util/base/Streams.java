package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符、字节流常用操作
 *
 * @author tbc on 2016/9/23 1:09.
 */
@Slf4j
@Deprecated
public class Streams {

    /**
     * 统一处理异常
     */
    private static void proException(String msg, Exception e) {
        msg = msg + " : " + e.getMessage();
        log.error(msg);
        throw new RuntimeException(msg, e);
    }


    /****************************************************/
    /******************* 公 共 API ***********************/
    /****************************************************/
    public static InputStreamReader toInputStreamReader(InputStream inputStream, Charset charset) {
        return new InputStreamReader(inputStream, charset);
    }

    public static BufferedReader toBufferedReader(InputStreamReader inputStreamReader) {
        return new BufferedReader(inputStreamReader);
    }

    public static BufferedReader toBufferedReader(InputStream inputStream, Charset charset) {
        return new BufferedReader(toInputStreamReader(inputStream, charset));
    }

    public static BufferedReader toBufferedReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }


    /****************************/
    public static List<String> readList(InputStreamReader inputStreamReader) {
        return new BufferedReader(inputStreamReader).lines().collect(Collectors.toList());
    }

    public static String read(InputStreamReader inputStreamReader) {
        return new BufferedReader(inputStreamReader).lines().collect(Collectors.joining("\n"));
    }

    public static String read(InputStream in, Charset charset) {
        return read(toInputStreamReader(in, charset));
    }

    public static String read(InputStream in) {
        return read(in, StandardCharsets.UTF_8);
    }

    public static String readToStringByCharset(InputStream inputStream, Charset charset) {
        Reader reader = new InputStreamReader(inputStream, charset);
        assert reader != null : "Assert : reader is null";

        BufferedReader bf = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String str = bf.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str).append("\n");
            } catch (IOException e) {
                proException("IO异常", e);
            }
        }
        return sb.toString();
    }
}
