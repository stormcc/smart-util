package github.com.stormcc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create By: Jimmy Song
 * Create At: 2023-07-12 00:11
 */
public final class CollectionUtil {
    private CollectionUtil(){}
    public static <T> List<T> newArrayList(T... array ){
        List<T> list = new ArrayList<>();
        if ( array == null  ) {
            return list;
        }
        for (T t : array) {
            list.add(t);
        }
        return list;
    }

    public static <T> Set<T> newLinkedHashSet(T... array ){
        Set<T> set = new LinkedHashSet<>();
        if ( array == null  ) {
            return set;
        }
        for (T t : array) {
            set.add(t);
        }
        return set;
    }

    public static <K, V> Map<K,V> newHashMap(K k, V v ){
        Map<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    public static <T> T getFirstOrNull(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
