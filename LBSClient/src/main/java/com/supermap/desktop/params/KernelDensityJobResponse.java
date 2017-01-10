package com.supermap.desktop.params;

import com.supermap.desktop.Interface.IResponse;

/**
 * Created by xie on 2017/1/10.
 * 核密度分析任务创建成功后返回的结果类
 */
public class KernelDensityJobResponse implements IResponse {
    public String postResultType;//创建任务类型
    public String newResourceID;//任务id
    public String succeed;//是否创建成功
    public String newResourceLocation;//新任务url
}
