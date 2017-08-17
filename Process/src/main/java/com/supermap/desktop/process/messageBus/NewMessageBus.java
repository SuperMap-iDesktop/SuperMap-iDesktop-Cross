package com.supermap.desktop.process.messageBus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.desktop.Application;
import com.supermap.desktop.lbs.IServerServiceImpl;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.progress.Interface.IUpdateProgress;
import com.supermap.desktop.utilities.StringUtilities;

import java.lang.reflect.Field;
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
	private volatile int percent = 0;
	private IUpdateProgress updateProgress;
	private IOpenServerMap openMap;

	public NewMessageBus(IResponse response, IUpdateProgress updateProgress, IOpenServerMap openMap) {
		this.response = response;
		this.updateProgress = updateProgress;
		this.openMap = openMap;
	}

	public boolean run() {
		boolean result = false;
		try {
			while (!stop) {
				result = excute(response);
			}
		} catch (InterruptedException exception) {
			Application.getActiveApplication().getOutput().output(exception);
		}
		return result;
	}


	private synchronized boolean excute(IResponse response) throws InterruptedException {
		boolean success = false;
		String queryInfo = serverService.query(((JobResultResponse) response).newResourceLocation);
		JobItemResultResponse result = null;
		if (!StringUtilities.isNullOrEmpty(queryInfo)) {
			result = JSON.parseObject(queryInfo, JobItemResultResponse.class);
		}
		if (null != result && "FAILED".equals(result.state.runState)) {
			this.updateProgress.updateProgress(100, "0", "Failed");
			Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_IServerBuildFailed"));
			stop = true;
			return success;
		} else if (null != result) {
			if (null != result.setting.serviceInfo && null != result.setting.serviceInfo.targetServiceInfos) {
				//获取iserver服务发布地址,并打开到地图，如果存在已经打开的地图则将iserver服务上的地图打开到当前地图
				ArrayList<IServerInfo> mapsList = result.setting.serviceInfo.targetServiceInfos;
				String serviceAddress = "";
				int size = mapsList.size();
				for (int i = 0; i < size; i++) {
					if ("RESTMAP".equals(mapsList.get(i).serviceType)) {
						serviceAddress = mapsList.get(i).serviceAddress;
					}
				}
				if (!StringUtilities.isNullOrEmpty(serviceAddress)) {
					success = true;
					Application.getActiveApplication().getOutput().output(ProcessProperties.getString("String_IServerBuildSuccess"));
					this.updateProgress.updateProgress(100, "0", "Finished");
					//获取查询iserver的结果
					stop = true;
					String datasourceName = "";
					try {
						Class<BigDataType> bigDataTypeClass = BigDataType.class;
						Field[] fields = bigDataTypeClass.getFields();
						for (Field field : fields) {
							BigDataType bigDataType = (BigDataType) field.get(bigDataTypeClass);
							if (serviceAddress.contains(bigDataType.getMessage())) {
								datasourceName = bigDataType.getResultName() + serviceAddress.substring(serviceAddress.indexOf(bigDataType.getMessage()) + bigDataType.getMessage().length()).replace("/rest", "");
								break;
							}
						}
					} catch (Exception e) {
						Application.getActiveApplication().getOutput().output(e);
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
							if (i > 0) {
								datasourceName += "_" + i;
							}
							this.openMap.openMap(iserverRestAddr, datasourceName, datasetName);
						}
					}
				}
			} else {
				if (percent <= 99) {
					this.updateProgress.updateProgress(new Random().nextInt(99), "", "Running");
				}
				Thread.sleep(100);
			}
		}
		return success;
	}
}
