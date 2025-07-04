package github.com.stormcc.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            map.put(s, map.getOrDefault(s, 0) +1);
        }
        return map;
    }

    public static void addToMap(Map<String, Integer> map, List<String> list) {
        for (String s : list) {
            map.put(s, map.getOrDefault(s, 0) +1);
        }
    }

    public static Map<Integer, List<Integer>> groupMapByValue(int[] array) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> list;
        for (int i = 0; i < array.length; i++) {
            list = map.getOrDefault(array[i], new ArrayList<>());
            list.add(i );
        }
        return map;
    }

    public static boolean equalIntegerMap(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        if ( map1 == null && map2 == null ) {
            return true;
        }
        if ( map1 == null || map2 == null ) {
            return false;
        }
        if ( map1.size() != map2.size() ){
            return false;
        }
        for (Integer k : map2.keySet()) {
            if ( ! map1.containsKey(k) ) {
                return false;
            }
            if ( ! map2.get(k).equals(map1.get(k))) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalStringMap(Map<String, Integer> map1, Map<String, Integer> map2) {
        if ( map1 == null && map2 == null ) {
            return true;
        }
        if ( map1 == null || map2 == null ) {
            return false;
        }
        if ( map1.size() != map2.size() ){
            return false;
        }
        for (String k : map2.keySet()) {
            if ( ! map1.containsKey(k) ) {
                return false;
            }
            if ( ! map2.get(k).equals(map1.get(k))) {
                return false;
            }
        }
        return true;
    }

    public <K,V> Map<K,V> sortMapByValue(Map<K, V> map){
        Stream<Map.Entry<K, V>> sorted = map.entrySet().stream().sorted((Comparator<? super Map.Entry<K, V>>) Map.Entry.comparingByValue());
        Map<K, V>  result = new LinkedHashMap<>();
        sorted.forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }
}
