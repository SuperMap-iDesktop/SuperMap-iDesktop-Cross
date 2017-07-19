package com.supermap.desktop.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtilties {
	public static boolean ping(String hostName) {
		return new Pinger(hostName, 2, 300).isReachable();
	}

	public static String getIpAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static String getMacAddress() throws SocketException, UnknownHostException {
		byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}

}

class Pinger {
	/**
	 * 要ping的主机
	 */
	private String remoteIpAddress;
	/**
	 * 设置ping的次数
	 */
	private final int pingTimes;
	/**
	 * 设置超时
	 */
	private int timeOut;

	/**
	 * 构造函数  *  * @param remoteIpAddress  * @param pingTimes  * @param timeOut
	 */
	public Pinger(String remoteIpAddress, int pingTimes, int timeOut) {
		this.remoteIpAddress = remoteIpAddress;
		this.pingTimes = pingTimes;
		this.timeOut = timeOut;
	}

	/**
	 * 测试是否能ping通  * @param server  * @param timeout  * @return
	 */
	public boolean isReachable() {
		BufferedReader in = null;
		Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是windows格式的命令
		String pingCommand = "ping " + remoteIpAddress + " -n " + pingTimes + " -w " + timeOut;
		try {
			// 执行命令并获取输出
			Process p = r.exec(pingCommand);
			if (p == null) {
				return false;
			}
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
			String line = null;
			while ((line = in.readLine()) != null) {
				if (getCheckResult(line)) {
					return true;
				}
			}
			// 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
		} catch (Exception ex) {
			ex.printStackTrace();   // 出现异常则返回假
			return false;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	/**
	 * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.  * @param line  * @return
	 */
	private static boolean getCheckResult(String line) {
		Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		return matcher.find();
	}

	public static void main(String[] args) {
		Pinger p = new Pinger("log.supermap.com", 4, 5000);
		System.out.println(p.isReachable());
	}
}