package github.com.stormcc.util;

import java.util.ArrayList;
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

    //使用场景：接口分页返回、批量数据分批处理
    public static <T> List<T> getPage(List<T> list, int pageNum, int pageSize) {
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= list.size()) {
            return new ArrayList<>(); // 返回空列表
        }
        int toIndex = Math.min(fromIndex + pageSize, list.size());
        return list.subList(fromIndex, toIndex);
    }
}
