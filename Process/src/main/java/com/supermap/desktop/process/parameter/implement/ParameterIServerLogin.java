package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.lbs.Interface.IServerService;
import com.supermap.desktop.ui.lbs.impl.IServerServiceImpl;
import com.supermap.desktop.ui.lbs.params.IServerLoginInfo;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author XiaJT
 */
public class ParameterIServerLogin extends ParameterCombine {
	private ParameterTextField parameterTextFieldAddress = new ParameterTextField(CoreProperties.getString("String_Server"));
	private ParameterTextField parameterTextFieldPort = new ParameterTextField(ProcessProperties.getString("String_port"));
	private ParameterTextField parameterTextFieldUserName = new ParameterTextField(ProcessProperties.getString("String_UserName"));
	private ParameterPassword parameterTextFieldPassword = new ParameterPassword(ProcessProperties.getString("String_PassWord"));

	public ParameterIServerLogin() {
		super();
		parameterTextFieldAddress.setSelectedItem("192.168.15.245");
		parameterTextFieldPort.setSelectedItem("8090");
		parameterTextFieldUserName.setSelectedItem("admin");
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
