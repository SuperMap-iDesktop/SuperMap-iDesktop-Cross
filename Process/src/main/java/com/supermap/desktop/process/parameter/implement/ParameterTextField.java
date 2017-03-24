package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterTextField extends AbstractParameter implements ISelectionParameter {
	private String describe;
	@ParameterField(name = "value")
	private String value;

	public ParameterTextField() {
		this("");
	}

	public ParameterTextField(String describe) {
		this.describe = describe;
	}

	@Override
	public String getType() {
		return ParameterType.TEXTFIELD;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, "value", oldValue, value));
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
