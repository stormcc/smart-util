package github.com.stormcc.dao;

/**
 * Create By: Jimmy Song
 * Create At: 2024-11-27 19:09
 */
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class NetworkDataDao {

    private static final String FILE_PATH = "/proc/net/dev";

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            // 跳过前两行，因为前两行不是网络接口数据
            br.readLine();
            br.readLine();
            while ((line = br.readLine())!= null) {
                // 以空格分割每行数据
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String interfaceName = parts[0].trim();
                    String[] stats = parts[1].trim().split("\\s+");
                    // 接收字节数在第1个位置，发送字节数在第9个位置（索引从0开始）
                    long receivedBytes = Long.parseLong(stats[1]);
                    long transmittedBytes = Long.parseLong(stats[9]);
                    System.out.println("Interface: " + interfaceName);
                    System.out.println("Received Bytes: " + receivedBytes);
                    System.out.println("Transmitted Bytes: " + transmittedBytes);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}