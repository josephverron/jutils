package pro.verron.jutils.property;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.String.join;
import static java.text.MessageFormat.format;

public class PropertyUtil {

    @SneakyThrows
    public static Map<String, String> load(InputStream is){
        Properties prop = new Properties();
        prop.load(is);
        TreeMap<String, String> map = new TreeMap<>();
        for (Map.Entry<Object, Object> entry : prop.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> load(InputStream... iss){
        Map<String, String> prop = new TreeMap<>();
        for (InputStream is : iss) {
            Map<String, String> current = load(is);
            if(haveDuplicateKeys(prop, current)){
                Set<String> duplicates = duplicateKeys(prop, current);
                String msg = format("Some keys are duplicated: {0}", join(",", duplicates));
                throw new PropertyException(msg);
            }
            prop.putAll(current);
        }
        return prop;
    }

    private static Set<String> duplicateKeys(Map<String, String> prop, Map<String, String> current) {
        Set<String> keys = prop.keySet();
        keys.retainAll(current.keySet());
        return keys;
    }

    private static boolean haveDuplicateKeys(Map<String, String> prop, Map<String, String> current) {
        return !duplicateKeys(prop, current).isEmpty();
    }

}
