package com.supermap.desktop.event;

import com.supermap.desktop.enums.GlobalParametersType;

/**
 * @author XiaJT
 */
public class GlobalParametersChangedEvent {

	private GlobalParametersType globalParametersType;
	private Object oldValue;
	private Object newValue;

	public GlobalParametersChangedEvent(GlobalParametersType globalParametersType, Object oldValue, Object newValue) {
		this.globalParametersType = globalParametersType;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public GlobalParametersType getGlobalParametersType() {
		return globalParametersType;
	}

	public void setGlobalParametersType(GlobalParametersType globalParametersType) {
		this.globalParametersType = globalParametersType;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
}
