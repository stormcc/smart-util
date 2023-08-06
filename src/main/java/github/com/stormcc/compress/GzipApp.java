package github.com.stormcc.compress;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Create By: Jimmy Song
 * Create At: 2023-08-06 11:03
 */
@Slf4j
public class GzipApp {
    private static final int LOOP_NUMBER = 5;
    private static String FILE_NAME="/Users/jimmysong/workspace/java/smart-util/README.md";
    private static String FILE_NAME1="/Users/jimmysong/workspace/java/smart-util/pom.xml";

    public static void main(String[] args) {
        testGzip( FILE_NAME);
        testGzip( FILE_NAME1);
    }

    private static void testDeflate(String fileName) {
        File ctqRequest = new File(fileName);
        StringBuffer sb = new StringBuffer();
        try (
            FileInputStream fin = new FileInputStream(ctqRequest);
            InputStreamReader reader = new InputStreamReader(fin, "UTF-8");){
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("originalLength:" + sb.toString().length());
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_NUMBER; i++) {
            String deflateCompress = ZipUtil.deflateCompress(sb.toString());
            ZipUtil.deflateUnCompress(deflateCompress);
            System.out.println("deflateLength:" + deflateCompress.length());
        }
        System.out.println("deflateCostAverageTime:" + (System.currentTimeMillis() - start) / 100.0);
    }

    private static void testLzo(String fileName) {
        File ctqRequest = new File(fileName);
        StringBuffer sb = new StringBuffer();
        try (FileInputStream fin = new FileInputStream(ctqRequest);
            InputStreamReader reader = new InputStreamReader(fin, "UTF-8");){
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("originalLength:" + sb.toString().length());
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_NUMBER; i++) {
            String lzoCompress = ZipUtil.lzoCompress(sb.toString());
            ZipUtil.lzoUnCompress(lzoCompress);
            System.out.println("lzoLength:" + lzoCompress.length());
        }
        System.out.println("lzoCostAverageTime:" + (System.currentTimeMillis() - start) / 100.0);
    }

    private static void testLzo4(String fileName) {
        File ctqRequest = new File(fileName);
        StringBuffer sb = new StringBuffer();
        try (FileInputStream fin = new FileInputStream(ctqRequest);
            InputStreamReader reader = new InputStreamReader(fin, "UTF-8");){
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("originalLength:" + sb.toString().length());
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_NUMBER; i++) {
            String lzo4Compress = ZipUtil.lz4Compress(sb.toString());
            ZipUtil.lz4UnCompress(lzo4Compress);
            System.out.println("lz4Length:" + lzo4Compress.length());
        }
        System.out.println("lz4CostAverageTime:" + (System.currentTimeMillis() - start) / 100.0);
    }
    private static void testSnappy(String fileName) {
        File ctqRequest = new File(fileName);
        StringBuffer sb = new StringBuffer();
        try (FileInputStream fin = new FileInputStream(ctqRequest);
            InputStreamReader reader = new InputStreamReader(fin, "UTF-8");){
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("originalLength:" + sb.toString().length());
        long start = System.currentTimeMillis();
        for(int i = 0;i < LOOP_NUMBER;i++) {
            String snappyCompress = ZipUtil.snappyCompress(sb.toString());
            ZipUtil.snappyUnCompress(snappyCompress);
            System.out.println("snappyLength:" + snappyCompress.length());
        }
        System.out.println("snappyCostAverageTime:" + (System.currentTimeMillis() - start) / 100.0);
    }

    private static void testGzip(String fileName) {
        log.info("fileName is:{}", fileName);
        File ctqRequest = new File(fileName);
        StringBuffer sb = new StringBuffer();
        try (FileInputStream fin = new FileInputStream(ctqRequest);
             InputStreamReader reader = new InputStreamReader(fin, "UTF-8");){
            while (reader.ready()) {
                sb.append((char) reader.read());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("originalLength:" + sb.toString().length());
        long start = System.currentTimeMillis();
        String s = sb.toString();
        for(int i = 0;i < LOOP_NUMBER; i++){
            String gzipCompress = ZipUtil.gzipCompress(sb.toString());
            String s1 = ZipUtil.gzipUnCompress(gzipCompress);
            if (s.equals(s1)) {
                if (s.length()> gzipCompress.length()) {
                    log.info("s length is:{}, gzipLength:{}", s.length(), gzipCompress.length());
                } else {
                    log.warn("s length is:{}, gzipLength:{}", s.length(), gzipCompress.length());
                }
            } else {
                log.error("s is:{}, s1 is:{}. gzipLength:{}" , s, s1, gzipCompress.length());
            }
        }
        System.out.println("gzipCostAverageTime:" + (System.currentTimeMillis() - start) / 100.0);
    }
}
