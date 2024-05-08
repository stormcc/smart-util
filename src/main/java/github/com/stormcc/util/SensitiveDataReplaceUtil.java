package github.com.stormcc.util;


import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.List;

/** 敏感信息模糊
 * Create By: Jimmy Song
 * Create At: 2024-05-08 10:39
 */
public class SensitiveDataReplaceUtil {
    private static final String SENSITIVE_DATA_REPLACEMENT_FULL = "******";
    private static final String SENSITIVE_DATA_REPLACEMENT_HALF = "****";
    private static final char SENSITIVE_DATA_REPLACEMENT_CHAR = '*';

    public static void main(String[] args) {
        List<String> list = Arrays.asList("13344445555", null, "1333", "13", "1334444555566", "13344446666");
        for (String s1 : list) {
            String s2 = sensitiveDataReplace(s1);
            System.out.println("s1:" + s1);
            System.out.println("s2:" + s2);
        }
    }

    public static String sensitiveDataReplace(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return SENSITIVE_DATA_REPLACEMENT_FULL;
        }
        if (str.length() < 6) {
            return str+SENSITIVE_DATA_REPLACEMENT_HALF;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i>=3 && i<=6){
                chars[i] =SENSITIVE_DATA_REPLACEMENT_CHAR;
            }
        }
        return String.valueOf(chars);
    }
}
