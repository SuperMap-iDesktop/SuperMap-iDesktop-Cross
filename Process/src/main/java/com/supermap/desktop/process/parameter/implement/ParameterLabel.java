package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterLabel extends AbstractParameter implements ISelectionParameter {
	private String describe;
	@ParameterField(name = PROPERTY_VALE)
	private String value = "";

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
	}

	@Override
	public Object getSelectedItem() {
		fireUpdateValue(PROPERTY_VALE);
		return value;
	}

	@Override
	public String getType() {
		return ParameterType.LABEL;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String getDescribe() {
		return describe;
	}
}
