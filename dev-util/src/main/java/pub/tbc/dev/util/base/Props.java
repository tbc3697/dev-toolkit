package pub.tbc.dev.util.base;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by tbc on 2018/1/17.
 */
@Slf4j
@Deprecated
public class Props {
    private Properties p = null;
    private String f = null;
    private boolean isAutoReload = false;


    private Props() {
    }

    private static class PropsFactory {
        static Props props = new Props();
    }

    public static Props getInstance() {
        return PropsFactory.props;
    }

    public Properties load(String file) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
            if (is == null) {
                throw new FileNotFoundException(file + " file is not found");
            }
            p = new Properties();
            p.load(is);
        } catch (Exception e) {
            log.error("load properties file failure ", e);
        }
        this.f = file;
        return p;
    }

    /**
     * 从绝对路径加载配置文件
     *
     * @param path
     * @param filename
     * @return
     * @author tbc
     * @version 1.0 2018年01月17日 22:43:32
     */
    public Properties load(String path, String filename) {
        String file = filename.endsWith(".properties") ? filename : filename + ".properties";
        return load(path + File.separator + file);
    }

    public Properties reload() {
        if (EmptyUtil.isEmpty(f)) {
            return null;
        }
        load(f);
        return p;
    }

    public void autoReload(int seconds, String threadName) {
        isAutoReload = true;
        Thread thread = new Thread(() -> {
            while (isAutoReload) {
                this.reload();
                Sleeps.seconds(seconds);
            }
        }, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    public void autoReload() {
        autoReload(3, "prop-auto-load");
    }

    public Properties getProperties() {
        return p;
    }

    public <T> T getValueByType(final String key, T defaultValue, Function<String, T> function) {
        T value = defaultValue;
        if (p.containsKey(key)) {
            value = function.apply(p.getProperty(key));
        }
        return value;
    }

    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * 获取字符型属性（可指定默认值）
     *
     * @param key
     * @param defaultValue
     * @return
     * @author tbc
     * @version 1.0 {2016年6月29日 下午5:10:03}
     */
    public String getString(String key, String defaultValue) {
        return getValueByType(key, defaultValue, input -> input);
    }

    public int getInt(Properties p, String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return getValueByType(key, defaultValue, CastUtil::castInt);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        return getValueByType(key, defaultValue, CastUtil::castDouble);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        return getValueByType(key, defaultValue, CastUtil::castBoolean);
    }

    public Map<String, String> filterKey(Predicate<String> predicate) {
        if (EmptyUtil.isEmpty(p)) {
            return null;
        }
        final Map<String, String> map = new HashMap();
        p.stringPropertyNames().stream().filter(predicate).forEach(k -> map.put(k, p.getProperty(k)));

        return map;
    }

    public Map<String, String> getMap(final String prefix) {
        return filterKey(input -> input.startsWith(prefix));
    }

    public Map<String, String> getMap() {
        return filterKey(input -> true);
    }

}
