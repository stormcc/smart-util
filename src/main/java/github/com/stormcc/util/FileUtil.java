package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Create By: Jimmy Song
 * Create At: 2022-09-05 18:15
 */
@Slf4j
public final class FileUtil {
    private FileUtil(){}

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


    public static List<String> tailLineList(String fileFullPath, int lineNumber) {
        List<String> list = new ArrayList<>(lineNumber);
        try (RandomAccessFile rf = new RandomAccessFile(fileFullPath, "r"); ) {
            long len = rf.length();
            long start = rf.getFilePointer();
            long nextEnd = start + len - 1;
            String line = null;
            rf.seek(nextEnd);
            int c = -1;
            while (nextEnd > start) {
                c = rf.read();
                if (c == '\n' || c == '\r') {
                    line = rf.readLine();
                    list.add(line);
                    if (list.size() == lineNumber) {
                        break;
                    }
                }
                nextEnd--;
                rf.seek(nextEnd);
            }
        } catch (FileNotFoundException e) {
            log.error("fileFullPath is:{}, lineNumber is:{}, FileNotFoundException is:{}",
                    fileFullPath, lineNumber, LogExceptionStackUtil.logExceptionStack(e));
        } catch (IOException e) {
            log.error("fileFullPath is:{}, lineNumber is:{}, IOException is:{}",
                    fileFullPath, lineNumber, LogExceptionStackUtil.logExceptionStack(e));
        }
        List<String> list1 = new ArrayList<>(lineNumber);
        for (int i = list.size()-1; i >=0; i--) {
            list1.add(list.get(i));
        }
        return list1;
    }
}
