package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;

/**
 * @author XiaJT
 */
public class ParameterUserDefine extends AbstractParameter {


	@Override
	public String getType() {
		return ParameterType.USER_DEFINE;
	}

	@Override
	public IParameterPanel getParameterPanel() {
		return panel;
	}

	public void setPanel(IParameterPanel panel) {
		this.panel = panel;
	}

	@Override
	public void dispose() {

	}

	@Override
	public String getDescribe() {
		return "UserDefine";
	}
}
