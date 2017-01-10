package com.supermap.desktop.Interface;

import com.supermap.desktop.params.KernelDensityJobSetting;
import com.supermap.desktop.params.KernelDensityJobResponse;
import org.apache.http.client.HttpClient;

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
     * 生成子（核密度分析）任务
     *
     * @param kernelDensityJobSetting 核密度分析参数
     * @return
     */
    KernelDensityJobResponse query(KernelDensityJobSetting kernelDensityJobSetting);

    /**
     * 查询核密度分析结果
     * @param newResourceLocation
     * @return
     */
    String query(String newResourceLocation);
}
