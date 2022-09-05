package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Create By: Jimmy Song
 * Create At: 2022-09-05 18:15
 */
@Slf4j
public class FileUtil {
    public static List<String> findAllFile(String path){
        List<String> list = new ArrayList<>();
        fillDeepFile(path, list);
        return list;
    }

    private static void fillDeepFile(String path, List<String> list){
        File parent = new File(path);
        if ( parent.isFile() ){
            return;
        }
        if ( parent.list() == null
                || parent.list().length == 0 ) {
            return;
        }
        log.info("path is:{}, list is:{}", path, parent.list());
        for ( String s : parent.list() ) {
            String fullPathName = path+"/"+s;
            File file = new File(fullPathName);
            if ( file.isFile()) {
                list.add(fullPathName);
            } else {
                fillDeepFile(fullPathName, list);
            }
        }
    }
}
