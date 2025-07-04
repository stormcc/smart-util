package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/** 表单验证、数据清洗、格式校验等。例如注册时验证用户输入的邮箱和手机号是否合法，避免无效数据入库
 * Create By: Jimmy Song
 * Create At: 2025-07-04 09:34
 */
@Slf4j
public class RegexUtil {
    // 邮箱验证
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    // 手机号验证（中国大陆）
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^1[3-9]\\d{9}$");

    // 身份证号验证（18位）
    private static final Pattern ID_CARD_PATTERN =
        Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");

    // 验证邮箱
    public static boolean isEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // 验证手机号
    public static boolean isPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    // 验证身份证号
    public static boolean isIdCard(String idCard) {
        return idCard != null && ID_CARD_PATTERN.matcher(idCard).matches();
    }
}
