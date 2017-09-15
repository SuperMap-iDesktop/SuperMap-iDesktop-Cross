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

	public ParameterIServerLogin() {
		super();
		parameterTextFieldAddress.setRequisite(true);
		parameterTextFieldAddress.setDefaultWarningValue("{ip}:{port}");
		//parameterTextFieldUserName.setDefaultWarningValue("admin");
		parameterTextFieldUserName.setRequisite(true);
		//parameterTextFieldPassword.setSelectedItem("map123!@#");
		parameterTextFieldPassword.setRequisite(true);
		this.addParameters(parameterTextFieldAddress, parameterTextFieldUserName, parameterTextFieldPassword);
		this.setDescribe(ProcessProperties.getString("String_loginInfo"));
		registerEvents();
	}

	public void setInputDataType(ParameterInputDataType inputDataType) {
		this.inputDataType = inputDataType;
	}

	private void registerEvents() {
		parameterTextFieldAddress.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				service = (IServerServiceImpl) login();
				if (null != IServerLoginInfo.client && null != evt.getNewValue()) {
					String newValue = evt.getNewValue().toString();
					String datasetsURL = service.HTTP_STR + newValue + DATASETS_URL;
					String resultDatasets = service.query(datasetsURL);
					QueryDatasetNamesResult queryDatasetNamesResult = JSON.parseObject(resultDatasets, QueryDatasetNamesResult.class);
					ParameterDataNode parameterDataNode = null;
					DatasetType[] datasetTypes = ParameterInputDataType.parameterSourceDataset.getDatasetTypes();
					if (null == inputDataType) {
						return;
					}
					for (int i = 0, size = queryDatasetNamesResult.datasetNames.size(); i < size; i++) {
						String resultDataset = service.query(service.HTTP_STR + newValue + DATASETS_URL + "/" + queryDatasetNamesResult.datasetNames.get(i));
						QueryDatasetTypeResult queryDatasetTypeResult = JSON.parseObject(resultDataset, QueryDatasetTypeResult.class);
						String datasetType = queryDatasetTypeResult.DatasetInfo.type;
						for (int j = 0; j < datasetTypes.length; j++) {
							if (datasetTypes[j].name().equalsIgnoreCase(datasetType)) {
								parameterDataNode = new ParameterDataNode(queryDatasetNamesResult.datasetNames.get(i), datasetType);
								inputDataType.bigDataStoreName.addItem(parameterDataNode);
							}
						}
					}
					inputDataType.bigDataStoreName.setSelectedItem(parameterDataNode);
				}

			}
		});
	}


	public synchronized IServerService login() {
		String username = (String) parameterTextFieldUserName.getSelectedItem();
		String password = (String) parameterTextFieldPassword.getSelectedItem();
		IServerService service = new IServerServiceImpl();
		String serviceInfo = (String) parameterTextFieldAddress.getSelectedItem();
		if (serviceInfo.contains(":") && serviceInfo.split(":").length > 1) {
			IServerLoginInfo.ipAddr = serviceInfo.split(":")[0];
			IServerLoginInfo.port = serviceInfo.split(":")[1];
		}
		IServerLoginInfo.username = username;
		IServerLoginInfo.password = password;
		CloseableHttpClient client = service.login(username, password);
		if (null != client) {
			IServerLoginInfo.client = client;
		} else {
			//throw new RuntimeException(ProcessProperties.getString("String_LoginFailed"));
		}
		return service;
	}

	public IServerService getService() {
		return service;
	}
}