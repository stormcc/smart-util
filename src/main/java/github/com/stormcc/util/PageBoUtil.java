package github.com.stormcc.util;

import github.com.stormcc.bo.PageBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By: Jimmy Song
 * Create At: 2023-06-21 17:49
 */
public final class PageBoUtil {

    private PageBoUtil(){}

    public static List<PageBo> createList(long totalNumber, int batchNumber){
        List<PageBo> list = new ArrayList<>();
        if ( totalNumber<=0 || batchNumber <=0 ) {
            return list;
        }
        long offset = 0;
        PageBo pageBo ;
        while (true) {
            pageBo = new PageBo();
            list.add(pageBo);
            pageBo.setOffset(offset);
            pageBo.setBatchNumber(batchNumber);
            offset+= batchNumber;
            if ( offset >=totalNumber) {
                break;
            }
        }
        return list;
    }
}
