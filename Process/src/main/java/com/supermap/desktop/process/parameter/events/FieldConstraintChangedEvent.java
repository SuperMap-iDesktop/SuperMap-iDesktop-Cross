package com.supermap.desktop.process.parameter.events;

import com.supermap.desktop.process.parameter.interfaces.IParameter;

/**
 * @author XiaJT
 */
public class FieldConstraintChangedEvent {
	private IParameter parameter;
	private String fieldName;

	public FieldConstraintChangedEvent(String fieldName, IParameter parameter) {
		this.fieldName = fieldName;
		this.parameter = parameter;
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
}
