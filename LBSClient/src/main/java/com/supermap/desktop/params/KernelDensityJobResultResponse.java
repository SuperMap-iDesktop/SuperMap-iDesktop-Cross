package com.supermap.desktop.params;

import com.supermap.desktop.Interface.IResponse;

/**
 * Created by xie on 2017/1/10.
 */
public class KernelDensityJobResultResponse implements IResponse {
    public String id;
    public KernelDensityJobResultState state = new KernelDensityJobResultState();
    public KernelDensityJobResultSetting setting = new KernelDensityJobResultSetting();
}
