package com.supermap.desktop.process.tasks;

/**
 * Created by highsad on 2017/6/22.
 */
public class SingleProgress {
	private boolean isIndeterminate;
	private int percent;
	private String message;
	private String remainTime;

	public SingleProgress(String message) {
		this.message = message;
		this.isIndeterminate = true;
	}

	public SingleProgress(int percent, String message, String remainTime) {
		this.percent = percent;
		this.message = message;
		this.remainTime = remainTime;
		this.isIndeterminate = false;
	}

	public int getPercent() {
		return percent;
	}

	public String getMessage() {
		return message;
	}

	public String getRemainTime() {
		return remainTime;
	}

	public boolean isIndeterminate() {
		return isIndeterminate;
	}
}
