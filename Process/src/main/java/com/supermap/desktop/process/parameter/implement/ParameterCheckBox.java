package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;

import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterCheckBox extends AbstractParameter implements ISelectionParameter {

	private Object value = "false";
	private String describe;
	private IParameters parameters;

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

	public IParameters getParameters() {
		return parameters;
	}

	public void setParameters(IParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public void dispose() {

	}
}
