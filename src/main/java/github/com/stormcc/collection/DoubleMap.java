package github.com.stormcc.collection;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Create By: Jimmy Song
 * Create At: 2023-08-03 22:13
 */
public class DoubleMap<I1, I2> {
    private final ConcurrentMap<I1, I2> map1;
    private final ConcurrentMap<I2, I1> map2;

    public DoubleMap(Map<I1, I2> map){
        map1 = new ConcurrentHashMap<>();
        map2 = new ConcurrentHashMap<>();
        map.forEach((k,v) ->{
            map1.put(k,v);
            map2.put(v,k);
        });
    }

    public I2 getFromItem1(I1 item1){
        return map1.get(item1);
    }

    public I1 getFromItem2(I2 item2){
        return map2.get(item2);
    }
}
