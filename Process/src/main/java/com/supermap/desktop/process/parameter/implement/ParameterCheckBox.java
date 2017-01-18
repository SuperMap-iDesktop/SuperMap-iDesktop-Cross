package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterCheckBoxPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterCheckBox extends AbstractParameter {

	private JPanel panel;
	private Object value;
	private String describe;

	@Override
	public String getType() {
		return ParameterType.CHECKBOX;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterCheckBoxPanel(this);
		}
		return panel;

	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		return value;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterCheckBox setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
