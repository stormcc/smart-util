package github.com.stormcc.collection;


import github.com.stormcc.exception.InvalidInputParameterException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Create By: Jimmy Song
 * Create At: 2023-08-03 22:13
 */
public class DoubleMap<I1, I2> {
    private ConcurrentMap<I1, I2> map1 = new ConcurrentHashMap<>();
    private ConcurrentMap<I2, I1> map2 = new ConcurrentHashMap<>();

    public I2 getFromItem1(I1 item1){
        return map1.get(item1);
    }

    public I1 getFromItem2(I2 item2){
        return map2.get(item2);
    }

    public static <I1, I2>  DoubleMap<I1, I2> build(List<I1> list1, List<I2> list2){
        if ( list1 == null || list1.isEmpty() ) {
            throw new InvalidInputParameterException("输入数据错误list1");
        }
        if ( list2 == null || list2.isEmpty() ) {
            throw new InvalidInputParameterException("输入数据错误list2");
        }
        if (list1.size() != list2.size()) {
            throw new InvalidInputParameterException("输入数据错误list2 list1");
        }
        DoubleMap<I1, I2> map = new DoubleMap<>();
        for (int i = 0; i < list1.size(); i++) {
            map.map1.put(list1.get(i), list2.get(i));
            map.map2.put(list2.get(i), list1.get(i));
        }
        return map;
    }

    public static <I1, I2>  DoubleMap<I1, I2> build(Map<I1, I2> data){
        if ( data == null || data.isEmpty() ) {
            throw new InvalidInputParameterException("输入数据错误data");
        }

        DoubleMap<I1, I2> map = new DoubleMap<>();
        data.forEach((k,v)->{
            map.map1.put(k,v);
            map.map2.put(v,k);
        });
        return map;
    }
}
