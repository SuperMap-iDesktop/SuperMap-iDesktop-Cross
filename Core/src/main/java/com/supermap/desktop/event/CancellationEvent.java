package com.supermap.desktop.event;

import java.util.EventObject;

public class CancellationEvent extends EventObject {

	private boolean isCancel = false;

	public CancellationEvent(Object source, boolean isCancel) {
		super(source);
		this.isCancel = isCancel;
	}

	public boolean isCancel() {
		return this.isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

}
