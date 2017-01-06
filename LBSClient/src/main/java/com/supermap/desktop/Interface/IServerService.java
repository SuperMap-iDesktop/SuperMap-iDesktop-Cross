package com.supermap.desktop.Interface;

import com.supermap.desktop.params.KernelDensityJobSetting;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

import java.security.NoSuchAlgorithmException;

/**
 * Created by xie on 2017/1/6.
 */
public interface IServerService {

    /**
     * 登录iserver服务
     *
     * @param userName
     * @param passWord
     * @return
     */
    HttpClient login(String userName, String passWord);

    /**
     * 查询核密度分析结果
     *
     * @param kernelDensityJobSetting 核密度分析参数
     * @return
     */
    String query(KernelDensityJobSetting kernelDensityJobSetting);
}
