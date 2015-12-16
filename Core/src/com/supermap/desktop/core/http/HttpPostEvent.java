package com.supermap.desktop.core.http;

import java.util.EventObject;

import com.supermap.desktop.core.FileSize;

public class HttpPostEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileSize totalSize = FileSize.ZERO;
	private FileSize postedSize = FileSize.ZERO;
	private double speed = 0d;
	private int remainTime = 0;
	private boolean isCancel = false;

	public HttpPostEvent(Object source, FileSize totalSize, FileSize postedSize, double speed, int remainTime) {
		super(source);
		this.totalSize = totalSize;
		this.postedSize = postedSize;
		this.speed = speed;
		this.remainTime = remainTime;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	public FileSize getTotalSize() {
		return totalSize;
	}

	public FileSize getPostedSize() {
		return postedSize;
	}

	public double getSpeed() {
		return speed;
	}

	public int getRemainTime() {
		return remainTime;
	}
}
