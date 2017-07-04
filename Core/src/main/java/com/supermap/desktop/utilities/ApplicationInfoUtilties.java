package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * @author XiaJT
 */
public class ApplicationInfoUtilties {
	private ApplicationInfoUtilties() {
	}

	public static String getLocalIp() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostAddress();
		} catch (UnknownHostException e) {
			Application.getActiveApplication().getOutput().output(e);
		}
		return "";
	}

	public static String getMacAddress() {
		try {

			byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
//			System.out.println("mac数组长度：" + mac.length);
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				//字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
//				System.out.println("每8位:" + str);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
//			System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
		} catch (Exception e) {

		}

		return "";
	}


	public static void main(String[] args) {
		System.out.println(getLocalIp());
	}

}
