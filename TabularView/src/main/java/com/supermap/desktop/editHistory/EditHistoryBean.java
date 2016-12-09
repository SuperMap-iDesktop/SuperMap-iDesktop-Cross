package com.supermap.desktop.editHistory;

/**
 * @author XiaJT
 */
public class EditHistoryBean {
	private int SmId;
	private String fieldName;
	private Object beforeValue;
	private Object afterValue;

	public EditHistoryBean(int smId, String fieldName, Object beforeValue, Object afterValue) {
		SmId = smId;
		this.fieldName = fieldName;
		this.beforeValue = beforeValue;
		this.afterValue = afterValue;
	}

	public int getSmId() {
		return SmId;
	}

	public void setSmId(int smId) {
		SmId = smId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(Object beforeValue) {
		this.beforeValue = beforeValue;
	}

	public Object getAfterValue() {
		return afterValue;
	}

	public void setAfterValue(Object afterValue) {
		this.afterValue = afterValue;
	}
}
