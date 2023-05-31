package github.com.stormcc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Create By: Jimmy Song
 * Create At: 2023-05-24 14:09
 */
public final class HttpServletRequestUtil {
    private HttpServletRequestUtil(){}

    public static String requestUrl(HttpServletRequest request){
        return request.getMethod() + " " + request.getRequestURI() + "?" + request.getQueryString();
    }
}
