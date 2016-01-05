package com.supermap.desktop.ui;

import java.util.EventObject;

public class StateChangeEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient TristateCheckBox.State state;

	public StateChangeEvent(Object source, TristateCheckBox.State state) {
		super(source);
		this.state = state;
	}

	public TristateCheckBox.State getState() {
		return this.state;
	}
}
