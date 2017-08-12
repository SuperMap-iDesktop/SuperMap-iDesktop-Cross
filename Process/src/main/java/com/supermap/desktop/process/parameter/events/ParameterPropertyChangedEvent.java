package com.supermap.desktop.process.parameter.events;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

/**
 * @author XiaJT
 */
public class ParameterPropertyChangedEvent {
	private IParameter parameter;
	private String propertyName;
	private Object oldValue;
	private Object currentValue;

	public ParameterPropertyChangedEvent(IParameter parameter, String propertyName, Object oldValue, Object currentValue) {
		this.parameter = parameter;
		this.propertyName = propertyName;
		this.oldValue = oldValue;
		this.currentValue = currentValue;
	}

	public IParameter getParameter() {
		return parameter;
	}

	public void setParameter(IParameter parameter) {
		this.parameter = parameter;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Object currentValue) {
		this.currentValue = currentValue;
	}
}
