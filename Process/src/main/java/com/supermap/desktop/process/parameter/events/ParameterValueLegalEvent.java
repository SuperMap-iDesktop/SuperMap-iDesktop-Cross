package com.supermap.desktop.process.parameter.events;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

/**
 * @author XiaJT
 */
public class ParameterValueLegalEvent {
	private IParameter parameter;
	private String fieldName;
	private Object parameterValue;
	private boolean isLegal = true;

	public ParameterValueLegalEvent(IParameter parameter, String fieldName, Object parameterValue) {
		this.parameter = parameter;
		this.fieldName = fieldName;
		this.parameterValue = parameterValue;
	}

	public IParameter getParameter() {
		return parameter;
	}

	public void setParameter(IParameter parameter) {
		this.parameter = parameter;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}

	public boolean isLegal() {
		return isLegal;
	}

	public void setLegal(boolean legal) {
		isLegal = legal;
	}
}
