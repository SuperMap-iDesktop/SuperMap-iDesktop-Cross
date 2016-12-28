package com.supermap.desktop.utilities;

import com.supermap.desktop.Application;
import com.supermap.desktop.properties.CommonProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        String result = null;
        if (SystemPropertyUtilities.isWindows()){
            result = System.getenv("COMPUTERNAME");
        }else{
            String command = "hostname";
            Process p;
            try {
                p = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    result = line;
                }
                br.close();
            } catch (IOException e) {
            }
        }
        return result;
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
     *return MAC address
     * @return
     */
    public static String getMACAddress() {
        String address = "";
        if (SystemPropertyUtilities.isWindows()) {
            try {
                InetAddress ip = InetAddress.getLocalHost();
                NetworkInterface ni = NetworkInterface.getByInetAddress(ip);
                byte[] mac = ni.getHardwareAddress();
                Formatter formatter = new Formatter();
                for (int i = 0; i < mac.length; i++) {
                    address = formatter.format(Locale.getDefault(), "%02X%s", mac[i],
                            (i < mac.length - 1) ? "-" : "").toString();

                }
            } catch (Exception e) {
                Application.getActiveApplication().getOutput().output(e);
            }
        } else{
            String command = "/bin/sh -c ifconfig -a";
            Process p;
            try {
                p = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    String info = "HWaddr";
                    String infoChina = CommonProperties.getString("String_ChinaMac");
                    if (line.contains(info)&&line.indexOf(info) > 0) {
                            int index = line.indexOf(info) + info.length();
                            address = line.substring(index);
                            break;
                    }else if(line.contains(infoChina)&&line.indexOf(infoChina) > 0){
                        int index = line.indexOf(infoChina) + infoChina.length();
                        address = line.substring(index);
                        break;
                    }
                }
                br.close();
            } catch (IOException e) {
            }
        }
        address = address.trim();
        return address;
    }

}
