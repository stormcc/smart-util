package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
/** 密码存储、数据完整性校验
 * Create By: Jimmy Song
 * Create At: 2025-07-04 09:36
 */
@Slf4j
public class Md5Util {

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            log.error("str is:{}, ", str, e);
            throw new RuntimeException(e);
        }
    }
}
