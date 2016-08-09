package com.supermap.desktop.mapview.layer.propertymodel;

import java.util.EventObject;

public class PropertyEnabledChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String propertyName = "";
	private Boolean enabled = true;

	public PropertyEnabledChangeEvent(Object source, String propertyName, Boolean enabled) {
		super(source);
		this.propertyName = propertyName;
		this.enabled = enabled;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Boolean getEnabled() {
		return enabled;
	}
}
