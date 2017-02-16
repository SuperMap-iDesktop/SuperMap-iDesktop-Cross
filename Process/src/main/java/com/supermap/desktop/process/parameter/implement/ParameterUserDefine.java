package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class ParameterUserDefine extends AbstractParameter {

	private JPanel panel;

	@Override
	public String getType() {
		return ParameterType.USER_DEFINE;
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void dispose() {

	}
}
