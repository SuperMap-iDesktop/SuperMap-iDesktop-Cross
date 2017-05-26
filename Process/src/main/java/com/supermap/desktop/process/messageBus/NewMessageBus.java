package com.supermap.desktop.process.messageBus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.desktop.Application;
import com.supermap.desktop.CommonToolkit;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.IResponse;
import com.supermap.desktop.ui.lbs.params.IServerInfo;
import com.supermap.desktop.ui.lbs.params.JobItemResultResponse;
import com.supermap.desktop.ui.lbs.params.JobResultResponse;
import com.supermap.desktop.utilities.StringUtilities;
import com.supermap.mapping.Map;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by xie on 2017/1/10.
 * 新的线程管理，通信类
 */
public class NewMessageBus {

	private IServerService serverService = new IServerServiceImpl();
	private IResponse response;
	private volatile boolean stop = false;
	private volatile IUpdateProgress task;
	private volatile int percent = 0;

	public NewMessageBus(IResponse response, IUpdateProgress task) {
		this.response = response;
		this.task = task;
	}

	public void run() {
		try {
			while (!stop) {
				excute(response);
			}
		} catch (InterruptedException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
	}


	private synchronized void excute(IResponse response) throws InterruptedException {
		String queryInfo = serverService.query(((JobResultResponse) response).newResourceLocation);
		JobItemResultResponse result = null;
		if (!StringUtilities.isNullOrEmpty(queryInfo)) {
			result = JSON.parseObject(queryInfo, JobItemResultResponse.class);
		}
//        if (null != result && "FINISHED".equals(result.state.runState)) {
		if (null != result) {
			if (null != result.setting.serviceInfo && null != result.setting.serviceInfo.targetServiceInfos) {
				task.updateProgress(100, "", "");
				// 获取iserver服务发布地址,并打开到地图，如果存在已经打开的地图则将iserver服务上的地图打开到当前地图
				ArrayList<IServerInfo> mapsList = result.setting.serviceInfo.targetServiceInfos;
				String serviceAddress = "";
				int size = mapsList.size();
				for (int i = 0; i < size; i++) {
					if ("RESTMAP".equals(mapsList.get(i).serviceType)) {
						serviceAddress = mapsList.get(i).serviceAddress;
					}
				}
				if (!StringUtilities.isNullOrEmpty(serviceAddress)) {
					//获取查询iserver的结果
					stop = true;
					String datasourceName = "";
					if (serviceAddress.contains("map-kernelDensity")) {
						datasourceName = serviceAddress.substring(serviceAddress.indexOf("map-kernelDensity")).replace("/rest", "");
					} else if (serviceAddress.contains("mongodb")) {
						datasourceName = serviceAddress.substring(serviceAddress.indexOf("mongodb")).replace("/rest", "");
					}
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
						openIserverMap(iserverRestAddr, datasourceName, datasetName);
					}
				}
			} else {
				if (percent <= 99) {
					task.updateProgress(new Random().nextInt(99), "", "");
				}
				Thread.sleep(100);
			}
		}
	}


	private static void openIserverMap(String iserverRestAddr, String datasourceName, final String datasetName) {
		DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
		connectionInfo.setEngineType(EngineType.ISERVERREST);
		connectionInfo.setServer(iserverRestAddr);
		connectionInfo.setAlias(datasourceName);
		Datasources datasources = Application.getActiveApplication().getWorkspace().getDatasources();
		if (null != datasources) {
			final Datasource datasource = datasources.open(connectionInfo);
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
				final Dataset finalDataset = dataset;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
//                        if (null != Application.getActiveApplication().getActiveForm() && Application.getActiveApplication().getActiveForm() instanceof IFormMap) {
//                            //添加到当前地图中
//                            Map currentMap = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().getMap();
//                            MapUtilities.addDatasetToMap(currentMap, finalDataset, true);
//                        } else {
						//打开新的地图
						IFormMap newMap = (IFormMap) CommonToolkit.FormWrap.fireNewWindowEvent(WindowType.MAP, datasetName + "@" + datasource.getAlias());
						Map map = newMap.getMapControl().getMap();
						map.getLayers().add(finalDataset, true);
						map.refresh();
						UICommonToolkit.getLayersManager().getLayersTree().reload();
//                        }
					}
				});

			}

		}
	}
}
