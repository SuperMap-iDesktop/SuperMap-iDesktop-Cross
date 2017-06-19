package com.supermap.desktop.process.enums;

/**
 * Created by highsad on 2017/6/15.
 */
public enum RunningStatus {
	NORMAL(0), RUNNING(1), COMPLETED(2), EXCEPTION(3), CANCELLED(4);

	private int value;

	RunningStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
