package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by xie on 2016/12/26.
 */
public class ComputerUtilities {
    /**
     * 获取计算机用户名
     *
     * @return
     */
    public static String getUserName() {
        return System.getenv("USERNAME");
    }

    /**
     * 获取计算机名
     *
     * @return
     */
    public static String getComputerName() {
        return System.getenv("COMPUTERNAME");
    }

    /**
     * 获取计算机用户域名
     *
     * @return
     */
    public static String getUserDomain() {
        return System.getenv("USERDOMAIN");
    }

    /**
     * 获取计算机IP
     *
     * @return
     */
    public static String getIp() {
        String sIP = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            sIP = address.getHostAddress();
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return sIP;
    }

    /**
     * 获取计算机MAC地址
     *
     * @return
     */
    public static String getMac() {
        String sMAC = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] mac = ni.getHardwareAddress();
            Formatter formatter = new Formatter();
            for (int i = 0; i < mac.length; i++) {
                sMAC = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                        (i < mac.length - 1) ? "-" : "").toString();

            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }
        return sMAC;
    }
}
