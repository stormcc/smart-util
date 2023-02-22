package github.com.stormcc.util;

import java.util.List;

/**
 * Create By: Jimmy Song
 * Create At: 2023-02-22 09:08
 */
public final class ListUtil {
    private ListUtil(){}

    public static int size(List list){
        if ( list == null ){
            return 0;
        } else {
            return list.size();
        }
    }

    public static int totalSize(List... lists){
        int sum =0;
        for (List list : lists) {
            if ( list != null) {
                sum+= list.size();
            }
        }
        return sum;
    }
}
