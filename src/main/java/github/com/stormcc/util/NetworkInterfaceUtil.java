package github.com.stormcc.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Create By: Jimmy Song
 * Create At: 2024-04-28 17:17
 */
@Slf4j
public final class NetworkInterfaceUtil {
    private static final String REGEX_IPV4 = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public static String getIpv4Address(String interfaceName, String defaultValue){
        if (Strings.isNullOrEmpty(interfaceName)) {
            return defaultValue;
        }
        // 获取本机所有的网络接口
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.error("SocketException is:{}", LogExceptionStackUtil.logExceptionStack(e));
            return defaultValue;
        }

        // 遍历所有的网络接口
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            if ( interfaceName.equals(ni.getName()) ) {
                Enumeration<InetAddress> enumeration = ni.getInetAddresses();
                while (enumeration.hasMoreElements()) {
                    InetAddress inetAddress = enumeration.nextElement();
                    String hostAddress =  inetAddress.getHostAddress();
                    if (!inetAddress.isLoopbackAddress() && isIPv4Address(hostAddress)) {
                        return hostAddress;
                    }
                }
            }
        }
        return defaultValue;
    }

    // 检查是否是IPv4地址
    public static boolean isIPv4Address(String ipv4Address) {
        return Pattern.matches(REGEX_IPV4, ipv4Address);
    }
}
