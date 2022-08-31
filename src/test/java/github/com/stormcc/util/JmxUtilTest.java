package github.com.stormcc.util;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Create By: Jimmy Song
 * Create At: 2022-08-31 13:34
 */
@Slf4j
public class JmxUtilTest extends TestCase {

    @Test
    public void testGetBeanAttributeValue() {
        String[] array = new String[]{};
        String value = JmxUtil.getBeanAttributeValue(array);
        System.out.println(value);
    }
}