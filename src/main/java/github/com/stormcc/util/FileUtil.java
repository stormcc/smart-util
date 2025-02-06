package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
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

    /**
     * 清理符合条件的老文件
     * @param dirPathName 目标目录路径
     * @param keyword 文件名包含的关键字
     * @param daysThreshold 过期天数阈值
     */
    public static void cleanFiles(String dirPathName, String keyword, int daysThreshold) throws IOException {
        Path directoryPath = Paths.get(dirPathName);

        // 验证目录存在
        if (!Files.exists(directoryPath) || !Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("无效的目录路径: " + dirPathName);
        }

        // 使用NIO的DirectoryStream遍历文件（非递归）
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path filePath : stream) {
                // 跳过目录和符号链接
                if (Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) continue;

                // 检查文件名
                String fileName = filePath.getFileName().toString();
                if (!fileName.contains(keyword)) continue;

                // 获取文件属性（避免跟踪符号链接）
                BasicFileAttributes attrs = Files.readAttributes(
                        filePath,
                        BasicFileAttributes.class,
                        LinkOption.NOFOLLOW_LINKS
                );

                // 计算文件年龄
                Instant lastModified = attrs.lastModifiedTime().toInstant();
                long daysOld = Duration.between(lastModified, Instant.now()).toDays();

                if (daysOld > daysThreshold) {
                    try {
                        boolean r = Files.deleteIfExists(filePath);
                        if (r) {
                            log.info("已删除文件: " + filePath);
                        }
                    } catch (NoSuchFileException e) {
                        log.error("文件不存在: " + filePath, e);
                    } catch (AccessDeniedException e) {
                        log.error("无删除权限: " + filePath, e);
                    } catch (IOException e) {
                        log.error("删除失败: " + filePath + " - ", e);
                    }
                }
            }
        }
    }
}
