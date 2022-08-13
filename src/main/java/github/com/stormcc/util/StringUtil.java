package github.com.stormcc.util;

public final class StringUtil {
    private StringUtil(){}

    public static boolean positiveInteger(String str) {
        return isPositiveNumber(str, 9);
    }

    public static boolean invalidPositiveInteger(String str) {
        if (positiveInteger(str)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean positiveSmallInteger(String str) {
        return isPositiveNumber(str, 4);
    }

    private static boolean isPositiveNumber(String str, int maxLength) {
        if (null == str){
            return false;
        }

        int length = str.length();
        if ( length > maxLength ){
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static String dataStringConvert(String srcStr, String ruleStr){
        String destStr = "";
        if (srcStr.contains(".")) { //整数
            String[] rules = ruleStr.split("\\.");
            if (rules.length > 1){
                String decimals = rules[1];
                if (decimals.length() == 1){
                    destStr = srcStr.substring(0, srcStr.length()-1);
                } else if (decimals.length() == 2){ //不需要处理
                    destStr = srcStr;
                }
            } else if(rules.length == 1) { //小数
                destStr = srcStr.split("\\.")[0];
            }
        }
        return destStr;
    }
}
