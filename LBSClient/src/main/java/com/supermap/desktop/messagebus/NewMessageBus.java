package com.supermap.desktop.messagebus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.Interface.ITask;
import com.supermap.Interface.ITaskFactory;
import com.supermap.Interface.TaskEnum;
import com.supermap.data.*;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.Interface.IResponse;
import com.supermap.desktop.Interface.IServerService;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.http.FileManagerContainer;
import com.supermap.desktop.impl.IServerServiceImpl;
import com.supermap.desktop.params.IServerInfo;
import com.supermap.desktop.params.JobItemResultResponse;
import com.supermap.desktop.params.JobResultResponse;
import com.supermap.desktop.task.TaskFactory;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.utilities.CommonUtilities;
import com.supermap.desktop.utilities.MapUtilities;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Map;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by xie on 2017/1/10.
 * 新的线程管理，通信类
 */
public class NewMessageBus {
    private static volatile ITask task;
    private static volatile ArrayList<IResponse> tasks = null;

    public static void producer(IResponse response) {
        try {
            if (null == tasks) {
                tasks = new ArrayList<>();
            }
            tasks.add(response);
            FileManagerContainer fileManagerContainer = CommonUtilities.getFileManagerContainer();
            ITaskFactory taskFactory = TaskFactory.getInstance();
            if (response instanceof JobResultResponse) {
                if (((JobResultResponse) response).newResourceLocation.contains("kernelDensity")) {
                    task = taskFactory.getTask(TaskEnum.KERNELDENSITYTASK, null);
                    addTask(fileManagerContainer, task);
                } else if (((JobResultResponse) response).newResourceLocation.contains("buildCache")) {
                    task = taskFactory.getTask(TaskEnum.HEATMAP, null);
                    addTask(fileManagerContainer, task);
                }
            }
            ExecutorService eService = Executors.newCachedThreadPool(new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("newMessagebus" + UUID.randomUUID());
                    return thread;
                }
            });
            eService.submit(new MessageBusConsumer());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addTask(FileManagerContainer fileManagerContainer, ITask task) throws InterruptedException {
        if (fileManagerContainer != null) {
            fileManagerContainer.addItem(task);
        }
    }

    public static class MessageBusConsumer implements Runnable, ExceptionListener {
        private IServerService serverService = new IServerServiceImpl();

        @Override
        public void run() {
            try {
                while (tasks.size() != 0) {
                    for (IResponse response : tasks) {
                        excute(response);
                    }
                }

            } catch (InterruptedException exception) {
                Application.getActiveApplication().getOutput().output(exception);
            }
        }


        private void excute(IResponse response) throws InterruptedException {
            String queryInfo = serverService.query(((JobResultResponse) response).newResourceLocation);
            JobItemResultResponse result = null;
            if (!StringUtilities.isNullOrEmpty(queryInfo)) {
                result = JSON.parseObject(queryInfo, JobItemResultResponse.class);
            }
            if (null != result && "FINISHED".equals(result.state.runState)) {
                task.updateProgress(100, "", "");
                if (null != result.setting.serviceInfo.targetServiceInfos) {
                    // 获取iserver服务发布地址,并打开到地图，如果存在已经打开的地图则将iserver服务上的地图打开到当前地图
                    ArrayList<IServerInfo> mapsList = result.setting.serviceInfo.targetServiceInfos;
                    String serviceAddress = "";
                    int size = mapsList.size();
                    for (int i = 0; i < size; i++) {
                        if ("RESTMAP".equals(mapsList.get(i).serviceType)) {
                            serviceAddress = mapsList.get(i).serviceAddress;
                            tasks.remove(response);
                        }
                    }
                    if (!StringUtilities.isNullOrEmpty(serviceAddress)) {
                        //获取查询iserver的结果
                        serviceAddress = serviceAddress + "/maps";
                        String mapsInfo = serverService.query(serviceAddress);
                        if (!StringUtilities.isNullOrEmpty(mapsInfo)) {
                            ArrayList<JSONObject> mapsResult = JSON.parseObject(mapsInfo, ArrayList.class);
                            String iserverRestAddr = "";
                            String datasetName = "";
                            int length = mapsResult.size();
                            for (int i = 0; i < length; i++) {
                                JSONObject object = mapsResult.get(i);
                                iserverRestAddr = (String) object.get("path");
                                datasetName = (String) object.get("name");
                            }
                            openIserverMap(iserverRestAddr, datasetName);
                        }
                    }
                }
            } else {
                updateProgress();
                Thread.sleep(100);
            }
        }

        @Override
        public void onException(JMSException exception) {
            Application.getActiveApplication().getOutput().output(exception);
        }
    }

    private static void openIserverMap(String iserverRestAddr, String datasetName) {
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setEngineType(EngineType.ISERVERREST);
        connectionInfo.setServer(iserverRestAddr);
        String datasourceName = "KernelDensity";
        Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
        if (null != datasources && null == datasources.get(datasourceName)) {
            connectionInfo.setAlias(datasourceName);
            Datasource datasource = datasources.open(connectionInfo);
            Dataset dataset = null;
            if (null == datasource.getDatasets().get(datasetName)) {
                return;
            } else {
                dataset = datasource.getDatasets().get(datasetName);
            }
            if (null == datasource) {
                Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceFaild"));
            } else {
                Application.getActiveApplication().getOutput().output(ControlsProperties.getString("String_OpenDatasourceSuccessful"));
                UICommonToolkit.refreshSelectedDatasourceNode(datasourceName);
                if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
                    //添加到当前地图中
                    Map currentMap = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
                    MapUtilities.addDatasetToMap(currentMap, dataset, true);
                } else {
                    //打开新的地图
                    IFormMap newMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, datasetName);
                    Map map = newMap.getMapControl().getMap();
                    MapUtilities.addDatasetToMap(map, dataset, true);
                    map.refresh();
                }
            }
        }
    }

    private static void updateProgress() throws InterruptedException {
        Thread.sleep(1000);
        task.updateProgress(getRandomProgress(), "", "");
    }

    private static int getRandomProgress() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(100);
    }
}
