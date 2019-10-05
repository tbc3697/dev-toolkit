package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 简单序列化工具
 * Created by tbc on 2017/6/11.
 */
@Slf4j
public class SerializeUtil {
    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static <T> byte[] serialize(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error("序列化失败： {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static <T> T unSerialize(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("反序列化失败： {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
