package github.com.stormcc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    public static boolean equal(int[] a1, int[] a2){
        if (a1.length != a2.length ) {
            return false;
        }
        for (int i = 0; i < a1.length; i++) {
            if ( a1[i] != a2[i] ) {
                return false;
            }
        }
        return true;
    }

    public static String toString(int[] array, String delimiter, String prefix, String suffix) {
        return toString0( array,  delimiter,  prefix,  suffix);
    }

    private static String toString0(int[] array, String delimiter, String prefix, String suffix) {
        if ( array.length == 0) {
            return "[]";
        }
        StringJoiner stringJoiner = new StringJoiner(delimiter,prefix, suffix);
        for (int i : array) {
            stringJoiner.add(String.valueOf(i));
        }
        return stringJoiner.toString();
    }

    public static String toString(int[] array) {
        return toString0(array, ",", "[", "]");
    }

    public static <K> List<K> arrayToList(K[] array) {
        List<K> list = new ArrayList<>();
        if (array == null) {
            return list;
        }
        list.addAll(Arrays.asList(array));
        return list;
    }

    public static <T> Map<T, Integer> count(List<T> list) {
        Map<T, Integer> map = new HashMap<>();
        for (T t : list) {
            map.put(t, map.getOrDefault(t, 0) + 1);
        }
        return map;
    }

    public static <T> List<T> removeDuplicates(List<T> list){
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}
