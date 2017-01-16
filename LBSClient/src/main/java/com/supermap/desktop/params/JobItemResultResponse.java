package com.supermap.desktop.params;

import com.supermap.desktop.Interface.IResponse;

/**
 * Created by xie on 2017/1/10.
 */
public class JobItemResultResponse implements IResponse {
    //{"id":"app-20170111184413-0006",
    // "state":{"errorStackTrace":null,"startTime":1484131453662,"endTime":1484131463955,"publisherelapsedTime":0,"runState":"FINISHED","errorMsg":null,"elapsedTime":10293},
    // "setting":{"output":{"cacheName":null,"outputPath":"/opt/supermap_iserver_811_14511_9_linux64_deploy/webapps/iserver/processingResultData/KernelDensity/grids/180ae86c-656e-4b69-9f2c-4fcd97a9d711","cacheType":null},
    // "args":["--input","/opt/LBSData/newyork_taxi_2013-01_14k.csv","--query","-74.050,40.550,-73.750,40.950","--resolution","0.004","--radius","0.004","--separator",",","--geoidx","10","--output","/opt/supermap_iserver_811_14511_9_linux64_deploy/webapps/iserver/processingResultData/KernelDensity/grids/180ae86c-656e-4b69-9f2c-4fcd97a9d711"],
    // "input":{"filePath":"/opt/LBSData/newyork_taxi_2013-01_14k.csv"},
    // "DEFAULT_MASTER_ADRESS":"local[*] ",
    // mainClass":"com.supermap.spark.main.KernelDensity",
    // "appName":"kernelDensity",
    // "analyst":{"query":"-74.050,40.550,-73.750,40.950","part":0,"geoidx":10,"radius":"0.004","resolution":"0.004","separator":",","multi":false},
    // "contextSetting":{"DEFAULT_EXECUTOR_MEMORY":"1g","executor_memory":"1g","driver_memory":"1g","DEFAULT_DRIVER_MEMORY":"1g","DEFAULT_EXECUTOR_CORES":1,"executor_cores":1},
    // "serviceInfo":null,"serviceRoot":"http://192.168.15.240:8090/iserver/services/"}}
    public String id;
    public JobItemResultState state = new JobItemResultState();
    public JobItemResultSetting setting = new JobItemResultSetting();
}
