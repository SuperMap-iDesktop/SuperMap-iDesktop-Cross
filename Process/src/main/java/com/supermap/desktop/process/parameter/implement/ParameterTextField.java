package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterTextFieldPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterTextField extends AbstractParameter {
	private String describe;
	private String value;
	private JPanel panel;

	@Override
	public String getType() {
		return ParameterType.TEXTFIELD;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterTextFieldPanel(this);
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

	public ParameterTextField setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}
