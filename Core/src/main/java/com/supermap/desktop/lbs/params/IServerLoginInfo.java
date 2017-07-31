package com.supermap.desktop.lbs.params;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by xie on 2017/1/6.
 * 登录信息类
 */
public class IServerLoginInfo {
    public static String ipAddr = "";
    public static CloseableHttpClient client;
    public static String username = "";
    public static String password = "";
    public static boolean login = false;
    public static boolean error = false;
    public static boolean saveLoginInfo = false;
    public static boolean remoteHost = false;
    public static String port = "";
}
