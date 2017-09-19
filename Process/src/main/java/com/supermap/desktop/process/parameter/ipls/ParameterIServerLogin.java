package com.supermap.desktop.process.parameter.ipls;

import com.alibaba.fastjson.JSON;
import com.supermap.data.DatasetType;
import com.supermap.desktop.lbs.IServerServiceImpl;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.IServerLoginInfo;
import com.supermap.desktop.lbs.params.QueryDatasetNamesResult;
import com.supermap.desktop.lbs.params.QueryDatasetTypeResult;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.StringUtilities;
import org.apache.http.impl.client.CloseableHttpClient;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterIServerLogin extends ParameterCombine {
	private ParameterDefaultValueTextField parameterTextFieldAddress = new ParameterDefaultValueTextField(CoreProperties.getString("String_Server"));
	private ParameterDefaultValueTextField parameterTextFieldUserName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));
	private static final String DATASETS_URL = "/iserver/services/datacatalog/rest/datacatalog/relationship/datasets";
	private IServerServiceImpl service;
	private ParameterInputDataType inputDataType;
	private ParameterInputDataType analystDataType;
	private DatasetType[] datasetTypes;
	private DatasetType[] analystDatasetTypes;

	public ParameterIServerLogin() {
		super();
		parameterTextFieldAddress.setRequisite(true);
		parameterTextFieldAddress.setDefaultWarningValue("{ip}:{port}");
		parameterTextFieldUserName.setRequisite(true);
		parameterTextFieldPassword.setRequisite(true);
		this.addParameters(parameterTextFieldAddress, parameterTextFieldUserName, parameterTextFieldPassword);
		this.setDescribe(ProcessProperties.getString("String_loginInfo"));
		registerEvents();
	}

	public void setAnalystDatasetTypes(DatasetType[] analystDatasetTypes) {
		this.analystDatasetTypes = analystDatasetTypes;
	}

	public void setAnalystDataType(ParameterInputDataType analystDataType) {
		this.analystDataType = analystDataType;
	}

	public void setInputDataType(ParameterInputDataType inputDataType) {
		this.inputDataType = inputDataType;
	}

	public void setDataType(DatasetType[] datasetType) {
		this.datasetTypes = datasetType;
	}

	private void registerEvents() {
		parameterTextFieldAddress.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				loginAndInitInputDataType();
			}
		});
		parameterTextFieldUserName.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				loginAndInitInputDataType();
			}
		});
		parameterTextFieldPassword.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				loginAndInitInputDataType();
			}
		});
	}

	private void loginAndInitInputDataType() {
		boolean result = login();
		if (!result) {
			removeAllDatasets();
			return;
		}
		if (null != IServerLoginInfo.client && null != parameterTextFieldAddress.getSelectedItem()) {
			removeAllDatasets();
			if (null != inputDataType) {
				initBigDataStoreName(inputDataType, datasetTypes);
			}
			if (null != analystDataType) {
				initBigDataStoreName(analystDataType, analystDatasetTypes);
			}
		}
	}

	private void initBigDataStoreName(ParameterInputDataType inputDataType, DatasetType[] datasetTypes) {
		String ipAndPort = parameterTextFieldAddress.getSelectedItem().toString();
		String datasetsURL = service.HTTP_STR + ipAndPort + DATASETS_URL;
		String resultDatasets = service.query(datasetsURL);
		QueryDatasetNamesResult queryDatasetNamesResult = JSON.parseObject(resultDatasets, QueryDatasetNamesResult.class);
		ParameterDataNode parameterDataNode = null;
		for (int i = 0, size = queryDatasetNamesResult.datasetNames.size(); i < size; i++) {
			String resultDataset = service.query(service.HTTP_STR + ipAndPort + DATASETS_URL + "/" + queryDatasetNamesResult.datasetNames.get(i));
			QueryDatasetTypeResult queryDatasetTypeResult = JSON.parseObject(resultDataset, QueryDatasetTypeResult.class);
			String datasetType = queryDatasetTypeResult.DatasetInfo.type;
			for (int j = 0, length = datasetTypes.length; j < length; j++) {
				if (datasetTypes[j].name().equalsIgnoreCase(datasetType)) {
					parameterDataNode = new ParameterDataNode(queryDatasetNamesResult.datasetNames.get(i), datasetType);
					inputDataType.bigDataStoreName.addItem(parameterDataNode);
				}
			}
		}
		inputDataType.bigDataStoreName.setSelectedItem(parameterDataNode);
	}


	private void removeAllDatasets() {
		if (inputDataType.bigDataStoreName.getItems().size() > 0) {
			inputDataType.bigDataStoreName.removeAllItems();
		}
		if (null != analystDataType && analystDataType.bigDataStoreName.getItems().size() > 0) {
			analystDataType.bigDataStoreName.removeAllItems();
		}
	}


	public synchronized boolean login() {
		boolean result = false;
		String username = (String) parameterTextFieldUserName.getSelectedItem();
		String password = (String) parameterTextFieldPassword.getSelectedItem();
		String serviceInfo = (String) parameterTextFieldAddress.getSelectedItem();
		if (StringUtilities.isNullOrEmpty(serviceInfo) || StringUtilities.isNullOrEmpty(username)
				|| StringUtilities.isNullOrEmpty(password)) {
			return result;
		}
		service = new IServerServiceImpl();
		if (serviceInfo.contains(":") && serviceInfo.split(":").length > 1) {
			IServerLoginInfo.ipAddr = serviceInfo.split(":")[0];
			IServerLoginInfo.port = serviceInfo.split(":")[1];
		}
		IServerLoginInfo.username = username;
		IServerLoginInfo.password = password;
		CloseableHttpClient client = service.login(username, password);
		if (null != client) {
			result = true;
			IServerLoginInfo.client = client;
		}
		return result;
	}

	public IServerService getService() {
		return service;
	}
}