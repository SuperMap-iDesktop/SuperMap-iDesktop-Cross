package com.supermap.desktop.process.tasks;

/**
 * Created by highsad on 2017/6/22.
 */
public class SingleProgress {
	private int percent;
	private String message;
	private String remainTime;

	public SingleProgress(int percent, String message, String remainTime) {
		this.percent = percent;
		this.message = message;
		this.remainTime = remainTime;
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
}
