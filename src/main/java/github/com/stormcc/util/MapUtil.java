package github.com.stormcc.util;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
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

    public static Map<String, Integer> map(List<String> list) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : list) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) +1);
            } else {
                map.put(s, 1);
            }
        }
        return map;
    }

    public static void addToMap(Map<String, Integer> map, List<String> list) {
        for (String s : list) {
            if (map.containsKey(s)) {
                map.put(s, map.get(s) +1);
            } else {
                map.put(s, 1);
            }
        }
    }
}
