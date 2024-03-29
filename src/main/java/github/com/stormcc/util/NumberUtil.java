package github.com.stormcc.util;

import com.google.common.base.Strings;
import github.com.stormcc.dto.RangeDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil {
    private NumberUtil(){}

    //字符串是否为纯数字
    private static final String REGEX_NUMBER = "^([0-9])+$";
    private static Pattern PATTERN_NUMBER = Pattern.compile(REGEX_NUMBER);
    private static String COMMA_SEPARATOR = ",";

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


    public static List<Short> uniqListOrEmptyList(String ids) {
        if ( Strings.isNullOrEmpty(ids) ) {
            return new ArrayList<>(0);
        }

        String[] arr = ids.split(COMMA_SEPARATOR);
        if ( arr.length <=0 ){
            return new ArrayList<>(0);
        }

        Set<Short> set = new HashSet<>();
        for (String s : arr) {
            if (  isPositiveInteger(s) ){
                set.add(Short.valueOf(s));
            }
        }
        if (set.size() == 0) {
            return new ArrayList<>(0);
        }
        List<Short> idList = new ArrayList<>(set);
        Collections.sort(idList);
        return idList;
    }


    public static List<RangeDto> rangeList(Integer maxId, Integer offset, Integer startId){
        List<RangeDto> list = new ArrayList<>();
        RangeDto rangeDto;
        int i=0;
        int start;
        int end;
        while (true) {
            start = i * offset+startId;
            if (start>maxId) {
                break;
            }
            end = (i+1) * offset+startId;
            if ( end > maxId ) {
                end = maxId;
            }
            rangeDto = new RangeDto();
            rangeDto.setStart(start);
            rangeDto.setEnd(end);
            list.add(rangeDto);
            i++;
        }
        return list;
    }

    public static List<Integer> uniqList(List<Integer> list){
        if (list == null || list.isEmpty() ) {
            return new ArrayList<>();
        }
        return new ArrayList<>(new LinkedHashSet<>(list));
    }
}
