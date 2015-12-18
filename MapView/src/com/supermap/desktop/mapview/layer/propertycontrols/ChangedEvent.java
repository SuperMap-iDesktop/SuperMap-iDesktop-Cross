package com.supermap.desktop.mapview.layer.propertycontrols;

import java.util.EventObject;

public class ChangedEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int CHANGED = 0;
	public static final int UNCHANGED = 1;
	public static final int TRISTATE = 2;

	private int preState = 0;
	private int currentState = 1;

	public ChangedEvent(Object source) {
		super(source);
	}

	public ChangedEvent(Object source, int currentState) {
		super(source);
		if (currentState < 0) {
			this.currentState = 0;
		} else if (currentState > 2) {
			this.currentState = 2;
		} else {
			this.preState = this.currentState;
			this.currentState = currentState;
		}
	}

	public int getPreState() {
		return this.preState;
	}

	public int getCurrentState() {
		return this.currentState;
	}
}
