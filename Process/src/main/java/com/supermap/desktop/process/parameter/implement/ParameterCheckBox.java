package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.constraint.annotation.ParameterField;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterCheckBox extends AbstractParameter implements ISelectionParameter {

	public static final String PARAMETER_CHECK_BOX_VALUE = "PARAMETER_CHECK_BOX_VALUE";
	@ParameterField(name = PARAMETER_CHECK_BOX_VALUE)
	private Object value = "false";
	private String describe;

	public ParameterCheckBox() {
		this("");
	}

	public ParameterCheckBox(String describe) {
		this.describe = describe;
	}

	@Override
	public String getType() {
		return ParameterType.CHECKBOX;
	}

	@Override
	public void setSelectedItem(Object value) {
		Object oldValue = this.value;
		this.value = String.valueOf(value);
		firePropertyChangeListener(new PropertyChangeEvent(this, PARAMETER_CHECK_BOX_VALUE, oldValue, value));
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
