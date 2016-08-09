package com.supermap.desktop.event;

import java.util.EventObject;

public class CancelEventObject extends EventObject {

	private boolean isCancel = false;
	
	public CancelEventObject(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isCancel() {
		return this.isCancel;
	}
	
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

}
