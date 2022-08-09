package github.com.stormcc.util;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public final class MapUtil {
    private MapUtil(){}

    public static Map<String, String> map(String content, String on, String keyValueSeparator){
        Map<String, String> map = new HashMap<>();
        String[] array = content.trim().split(on);
        if (array.length == 0 ) {
            return map;
        }
        String[] kvs;
        for (String s : array) {
            kvs = s.trim().split(keyValueSeparator, 2);
            if (kvs.length == 0 ) {
                continue;
            }
            String tmp = kvs[0].trim();
            if (Strings.isNullOrEmpty(tmp)) {
                continue;
            }
            if (kvs.length ==1 ) {
                map.put(tmp, null);
            } else{
                map.put(tmp, kvs[1]);
            }
        }
        return map;
    }
}
