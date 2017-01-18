package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterEnumPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterEnum extends AbstractParameter {

	// todo 枚举类型如何资源化
	private JPanel panel;
	private Class enumClass;
	private Object value;
	private String describe;
//	private Class enumDescribeClassName;

	@Override
	public String getType() {
		return ParameterType.ENUM;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterEnumPanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = value;
		firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		return value;
	}

	public Class getEnumClass() {
		return enumClass;
	}

	public ParameterEnum setEnumClass(Class enumClass) {
		this.enumClass = enumClass;
		return this;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterEnum setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
