package com.supermap.desktop.utilities;

import com.supermap.desktop.properties.CoreProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkUtilties {
	public static boolean ping(String hostName) {
        return new Pinger(hostName, 2, 300).isReachable();
    }

	public static String getIpAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static String getMacAddress() throws SocketException, UnknownHostException {
        if (SystemPropertyUtilities.isLinux()) {
            return getLinuxMacAddress();
        } else if (SystemPropertyUtilities.isLinux()) {
            return getWindows7MacAddress();
        }
        return "";
    }

    private static String getWindows7MacAddress() throws SocketException, UnknownHostException {
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

    private static String[] linuxMacAddressNames = new String[]{
            "hwaddr", CoreProperties.getString("String_MacAddress")
    };

    private static String getLinuxMacAddress() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {
            // linux下的命令，一般取eth0作为本地主网卡
            process = Runtime.getRuntime().exec("ifconfig eth0");
            // 显示信息中包含有mac地址信息
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串
                for (String linuxMacAddressName : linuxMacAddressNames) {
                    index = line.toLowerCase().indexOf(linuxMacAddressName);
                    if (index >= 0) {// 找到了
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + linuxMacAddressName.length() + 1).trim();
                        break;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return mac;
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
        String pingCommand = "ping " + remoteIpAddress;
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
     * 若line含有18ms字样,说明已经ping通,返回1,否則返回0.  * @param line  * @return
     */
	private static boolean getCheckResult(String line) {
        return line.toLowerCase().contains("ms");
    }

	public static void main(String[] args) {
		Pinger p = new Pinger("log.supermap.com", 4, 5000);
		System.out.println(p.isReachable());
	}
}