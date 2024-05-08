package github.com.stormcc.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Create By: Jimmy Song
 * Create At: 2024-05-08 11:13
 */
public class SensitiveDataReplaceUtilTest {

    @Test
    public void sensitiveDataReplaceTest() {
        List<String> list = Arrays.asList("13344445555", null, "1333", "13", "1334444555566", "13344446666");
        for (String s1 : list) {
            String s2 = SensitiveDataReplaceUtil.sensitiveDataReplace(s1);
            System.out.println("s1:" + s1);
            System.out.println("s2:" + s2);
            System.out.println("");
        }
    }
}