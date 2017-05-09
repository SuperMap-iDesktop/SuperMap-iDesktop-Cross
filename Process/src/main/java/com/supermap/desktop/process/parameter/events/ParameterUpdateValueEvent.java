package com.supermap.desktop.process.parameter.events;

/**
 * Created by hanyz on 2017/5/9.
 */
public class ParameterUpdateValueEvent {
	private String fieldName;

	public ParameterUpdateValueEvent(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
