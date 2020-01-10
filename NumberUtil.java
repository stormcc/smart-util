package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NumberUtil {
    //字符串是否为纯数字
    private static final String REGEX_NUMBER = "^([0-9])+$";
    private static Pattern PATTERN_NUMBER = Pattern.compile(REGEX_NUMBER);

    public static boolean validNumber(String number){
        if ((number == null) || (number.isEmpty())) {
            return false;
        }
        Matcher mat = PATTERN_NUMBER.matcher(number);
        return mat.find();
    }

    public static String getNumberInThreshold(int max, String number){
        if (! validNumber(number)){
            return String.valueOf(max);
        }
        int num = Integer.parseInt(number);
        num = num < max? num:max;
        return String.valueOf(num);
    }
}
