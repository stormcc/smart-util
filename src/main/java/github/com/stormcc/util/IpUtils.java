package github.com.stormcc.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
@Slf4j
public final class IpUtils {
	private IpUtils(){}

	private final static String ipTemplate = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

	/**
	 * 获得本机名
	 * @return
	 */
	public static String getHostName(){
		try{
			return InetAddress.getLocalHost().getHostName();
		}catch (UnknownHostException e){
			log.error("getHostName error. {}", LogExceptionStackUtil.logExceptionStack(e));
		}
		return null;
	}

	/**
	 * 获得本机pid
	 * @return
	 */
	public static int getPid(){
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		try{
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		}catch (Exception e) {
			log.error("getPid error. {}", LogExceptionStackUtil.logExceptionStack(e));
		}
		return -1;
	}

	/**
	 * 获得本机ip地址
	 * @return
	 */
	public static String getLocalIpV4(){
		return getLocalIpV4(null);
	}

	/**
	 * 获得本机ip地址
	 * @param tryIp
	 * @return
	 */
	public static String getLocalIpV4(String tryIp){
		Enumeration<NetworkInterface> networkInterface;
		try{
			networkInterface = NetworkInterface.getNetworkInterfaces();
		}catch (SocketException e){
			throw new IllegalStateException(e);
		}
		String ip = null;
		while (networkInterface.hasMoreElements()){
			NetworkInterface ni = (NetworkInterface)networkInterface.nextElement();
			Enumeration<InetAddress> inetAddress = ni.getInetAddresses();
			while (inetAddress.hasMoreElements()){
				InetAddress ia = (InetAddress)inetAddress.nextElement();
				if (!(ia instanceof Inet6Address)){
					String thisIp = ia.getHostAddress();
					if ((!ia.isLoopbackAddress()) && (!thisIp.contains(":")) && (!"127.0.0.1".equals(thisIp)) && ((isIntranetIpv4(thisIp)) || (ip == null))){
						ip = thisIp;
						if (ip.equals(tryIp)) {
							return tryIp;
						}
					}
				}
			}
		}
		return ip;
	}
	
	/**
	 * 判断内网地址
	 * @param ip
	 * @return
	 */
	private static boolean isIntranetIpv4(String ip){
		return  ip.startsWith("10.")
				|| ip.startsWith("192.168.")
				|| ip.matches("^172.(1[6-9]]|2|3[0-1])") ;
	}

	/**
	 * 获得到远程用户IP
	 * @param httpRequest
	 * @return
	 */
	public static String GetUserIp(HttpServletRequest httpRequest){
		String ip = httpRequest.getHeader("X-Real-IP");
		if (ip == null
				|| ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip) 
				|| isIntranetIpv4(ip)
				) {			
			ip = httpRequest.getHeader("X-Forwarded-For");
		}
		if (ip == null
				|| ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = httpRequest.getRemoteAddr();
		}
		String[] ips = null;
		if (ip != null) {
			ips = ip.split(",");
			ip = ips[0];
		}
		return ip;
	}

	public static String getHostIpV4(){
		Enumeration<NetworkInterface> allNetInterfaces = null;
		try {
			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (Exception e) {
			return null;
		}
		InetAddress ip = null;
		while ( allNetInterfaces.hasMoreElements() ){
			NetworkInterface netInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
			while ( addresses.hasMoreElements() ){
				ip = addresses.nextElement();
				if ( ip != null
						&& ip instanceof Inet4Address
						&& !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1 ){
					return ip.getHostAddress();
				}
			}
		}
		return null;
	}

	public static long ipv4String2Long(String ipStr){
		String[] ips = ipStr.split("\\.");
		return (Long.parseLong(ips[0])<<24) + (Long.parseLong(ips[1]) << 16 )
				+ (Long.parseLong(ips[2])<<8) + (Long.parseLong(ips[3])  );
	}

	public static String ipv4Long2String(long ipLong){
		StringBuilder sb = new StringBuilder();
		sb.append(ipLong >>> 24).append(".");
		sb.append((ipLong >>> 16)& 0xFF).append(".");
		sb.append((ipLong >>> 8) & 0xFF).append(".");
		sb.append(ipLong & 0xFF );
		return sb.toString();
	}

	public static boolean validIpv4String(String ipStr) {
		Pattern pattern = Pattern.compile(ipTemplate);
		Matcher matcher = pattern.matcher(ipStr);
		return matcher.matches();
	}
}
