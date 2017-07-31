package com.supermap.desktop.process.parameter.ipls;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterPassword extends AbstractParameter implements ISelectionParameter {
	private String describe;

	@ParameterField(name = PROPERTY_VALE)
	private String value = "";

	public ParameterPassword() {
		this("");
	}

	public ParameterPassword(String describe) {
		this.describe = describe;
	}

	@Override
	public void setSelectedItem(Object item) {
		Object oldValue = this.value;
		value = (String) item;
		firePropertyChangeListener(new PropertyChangeEvent(this, PROPERTY_VALE, oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		fireUpdateValue(PROPERTY_VALE);
		return value;
	}

	@Override
	public String getType() {
		return ParameterType.PASSWORD;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}
}
