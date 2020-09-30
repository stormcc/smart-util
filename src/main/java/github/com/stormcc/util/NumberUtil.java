package github.com.stormcc.util;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
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

    public static boolean isPositiveInteger(String str) {
        if ( (null == str) || (str.isEmpty()) ) {
            return false;
        }
        int len = str.length();

        if ( len > 10) {
            return false;
        }
        for (int i = len-1; i >=0; i-- ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        if ( len == 10 ) {
            String prefix = str.substring(0,5);
            if (Integer.parseInt(prefix) >= 21475 ) {
                return false;
            }
            String postfix = str.substring(5, 10);
            if ( Integer.parseInt(postfix) > 83647 ) {
                return false;
            }
        }
        return true;
    }

    public static Integer maxOrElse(List<Integer> list, Integer defaultValue){
        if ( list == null || list.isEmpty() ) {
            return defaultValue;
        }
        int size = list.size();
        if ( size == 1 ){
            return list.get(0);
        }
        Integer max = list.get(0);
        for (int i=1; i<size; i++ ) {
            if ( list.get(i) == null ) {
                continue;
            }
            if (max == null)  {
                max = list.get(i);
                continue;
            }
            if ( max<list.get(i) )  {
                max = list.get(i);
            }
        }
        if ( max == null ) {
            return defaultValue;
        }
        return max;
    }

    public static List<Integer> toPositiveList(String s, String separator){
        if ( Strings.isNullOrEmpty(s) ){
            return new ArrayList<>();
        }
        String[] array = s.trim().split(separator);
        if ( array.length <1 ) {
            return new ArrayList<>();
        }
        List<Integer> list = new ArrayList<>();
        for (String s1 : array) {
            if ( ! isPositiveInteger(s1) ) {
                continue;
            }
            list.add(Integer.valueOf(s1));
        }
        return list;
    }

    /** 未考虑负整数 **/
    public static boolean isInteger(String str) {
        if ( (null == str) || (str.isEmpty()) ) {
            return false;
        }

        int len = str.length();

        if ( len > 10) {
            return false;
        }
        for (int i = str.length()-1; i >=0; i-- ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> toList(String s, String separator){
        if ( Strings.isNullOrEmpty(s) ){
            return new ArrayList<>();
        }
        String[] array = s.trim().split(separator);
        if ( array.length <1 ) {
            return new ArrayList<>();
        }
        List<Integer> list = new ArrayList<>();
        for (String s1 : array) {
            if ( ! isInteger(s1) ) {
                continue;
            }
            list.add(Integer.valueOf(s1));
        }
        return list;
    }
}
