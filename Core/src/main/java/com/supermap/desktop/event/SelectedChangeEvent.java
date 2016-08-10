package com.supermap.desktop.event;

import java.util.EventObject;

public class SelectedChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSelected = false;

	public SelectedChangeEvent(boolean isSelected) {
		super(null);
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return this.isSelected;
	}
}
