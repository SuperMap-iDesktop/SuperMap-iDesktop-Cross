package com.supermap.desktop.Interface;

import com.supermap.desktop.params.BuildCacheJobSetting;
import com.supermap.desktop.params.KernelDensityJobSetting;
import com.supermap.desktop.params.JobResultResponse;
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
    JobResultResponse query(KernelDensityJobSetting kernelDensityJobSetting);

    /**
     * 生成子（热度图）任务
     * @param buildCacheJobSetting
     * @return
     */
    JobResultResponse query(BuildCacheJobSetting buildCacheJobSetting);
    /**
     * 查询JSON结果
     * @param url
     * @return
     */
    String query(String url);

}
