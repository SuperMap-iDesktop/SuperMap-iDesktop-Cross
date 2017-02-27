package com.supermap.desktop.ui.lbs.params;


/**
 * Created by xie on 2017/1/10.
 * 分析任务创建成功后返回的结果类
 */
public class JobResultResponse implements IResponse {
    //{"postResultType":"CreateChild",
    // "newResourceID":"app-20170111170553-0018",
    // "succeed":true,
    // "newResourceLocation":"http://192.168.15.240:8090/iserver/services/processing/rest/v1/jobs/mapping/buildCache/app-20170111170553-0018"}
    public String postResultType;//创建任务类型
    public String newResourceID;//任务id
    public String succeed;//是否创建成功
    public String newResourceLocation;//新任务url
}
