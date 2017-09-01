package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IProcess;

import java.util.EventObject;

/**
 * Created by highsad on 2017/1/5.
 */
public class RunningEvent extends EventObject {

	private IProcess process;
	private boolean isIndeterminate = false;
	private int progress;
	private String message;
	private int remainTime; // milliseconds
	private int totalTime; // milliseconds
	private boolean isCancel = false;

	public RunningEvent(IProcess process, String message) {
		super(process);
		this.process = process;
		this.message = message;
		this.isIndeterminate = true;
	}

	public RunningEvent(IProcess process, int progress, String message) {
		this(process, progress, message, 0, 0);
	}

	public RunningEvent(IProcess process, int progress, String message, int remainTime) {
		this(process, progress, message, remainTime, 0);
	}

	public RunningEvent(IProcess process, int progress, String message, int remainTime, int totalTime) {
		super(process);
		this.progress = progress;
		this.message = message;
		this.remainTime = remainTime;
		this.totalTime = totalTime;
		this.isIndeterminate = false;
	}

	public IProcess getProcess() {
		return process;
	}

	public int getProgress() {
		return progress;
	}

	public String getMessage() {
		return message;
	}

	public int getRemainTime() {
		return remainTime;
	}

	public int getTotalTime() {
		return totalTime;
	}

	/**
	 * 进度信息与进度值是否不确定
	 *
	 * @return
	 */
	public boolean isIndeterminate() {
		return isIndeterminate;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean cancel) {
		isCancel = cancel;
	}
}
