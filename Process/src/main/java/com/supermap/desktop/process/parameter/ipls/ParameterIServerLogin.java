package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.lbs.IServerServiceImpl;
import com.supermap.desktop.lbs.Interface.IServerService;
import com.supermap.desktop.lbs.params.IServerLoginInfo;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CoreProperties;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author XiaJT
 */
public class ParameterIServerLogin extends ParameterCombine {
	private ParameterDefaultValueTextField parameterTextFieldAddress = new ParameterDefaultValueTextField(CoreProperties.getString("String_Server"));
	private ParameterDefaultValueTextField parameterTextFieldPort = new ParameterDefaultValueTextField(ProcessProperties.getString("String_port"));
	private ParameterDefaultValueTextField parameterTextFieldUserName = new ParameterDefaultValueTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

	public ParameterIServerLogin() {
		super();
		parameterTextFieldAddress.setDefaultWarningValue("192.168.15.248");
		parameterTextFieldPort.setDefaultWarningValue("8090");
		parameterTextFieldUserName.setDefaultWarningValue("admin");
		parameterTextFieldPassword.setSelectedItem("map123!@#");
		this.addParameters(parameterTextFieldAddress, parameterTextFieldPort, parameterTextFieldUserName, parameterTextFieldPassword);
		this.setDescribe(ProcessProperties.getString("String_loginInfo"));
	}

	public IServerService login() {
		String username = (String) parameterTextFieldUserName.getSelectedItem();
		String password = (String) parameterTextFieldPassword.getSelectedItem();
		IServerService service = new IServerServiceImpl();
		IServerLoginInfo.ipAddr = (String) parameterTextFieldAddress.getSelectedItem();
		IServerLoginInfo.port = (String) parameterTextFieldPort.getSelectedItem();
		CloseableHttpClient client = service.login(username, password);
		if (null != client) {
			IServerLoginInfo.client = client;
		} else {
			throw new RuntimeException(ProcessProperties.getString("String_LoginFailed"));
		}
		return service;
	}
}
