package com.supermap.desktop.netservices.iserver;

import java.util.EventObject;

public class FunctionProgressEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int currentProgress = 0;
	private int totalProgress = 0;
	private String currentMessage = "";
	private String totalMessage = "";
	private boolean isCancel = false;

	public FunctionProgressEvent(Object source, int totalProgress, int currentProgress, String currentMessage, String totalMessage) {
		super(source);
		this.currentProgress = currentProgress;
		this.totalProgress = totalProgress;
		this.currentMessage = currentMessage;
		this.totalMessage = totalMessage;
	}

	public int getCurrentProgress() {
		return currentProgress;
	}

	public int getTotalProgress() {
		return totalProgress;
	}

	public String getCurrentMessage() {
		return currentMessage;
	}

	public String getTotalMessage() {
		return totalMessage;
	}

	public boolean isCancel() {
		return this.isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}
}
