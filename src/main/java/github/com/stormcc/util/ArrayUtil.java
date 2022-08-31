package github.com.stormcc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-31 13:37
 */
public final class ArrayUtil {
    private ArrayUtil(){}

    public static List<Integer> toIntegerList(int[] array){
        if ( array.length == 0 ) {
            return new ArrayList<>();
        }
        List<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }

    public static boolean diffFromBeforeAll(List<int[]> list, int one){
        if ( one == 0 ) {
            return true;
        }
        boolean[] diff = new boolean[one];
        int[] a0 = list.get(one);
        for (int i = 0; i < one; i++) {
            int[] a = list.get(i);
            for (int j = 0; j < a.length; j++) {
                if ( a[j] != a0[j] ) {
                    diff[i] = true;
                    break;
                }
            }
        }
        boolean r = true;
        for (int i = 0; i < one; i++) {
            r = r && diff[i];
        }
        return r;
    }
}
