package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;


/**
 * Create By: Jimmy Song
 * Create At: 2022-09-05 18:23
 */
@Slf4j
public class FileUtilTest {

    public void init(){
        log.info("init.");
    }

    @Test
    public void findAllFile() {
        String path = "/Users/jimmysong/Downloads/nginx";
        log.info("start...path is:{}", path);
        List<String> list = FileUtil.findAllFile(path);
        for (String s : list) {
            log.info("s is:{}", s);
        }
    }
}