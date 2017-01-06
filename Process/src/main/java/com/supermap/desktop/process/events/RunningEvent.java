package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IProcess;

import java.util.EventObject;

/**
 * Created by highsad on 2017/1/5.
 */
public class RunningEvent extends EventObject {

	private IProcess process;
	private int progress;
	private String message;
	private int remainTime; // milliseconds
	private int totalTime; // milliseconds

	public RunningEvent(IProcess process) {
		this(process, 0, "", 0, 0);
	}

	public RunningEvent(IProcess process, int progress, String message) {
		this(process, progress, message, 0, 0);
	}

	public RunningEvent(IProcess process, int progress, String message, int remainTime, int totalTime) {
		super(process);
		this.progress = progress;
		this.message = message;
		this.remainTime = remainTime;
		this.totalTime = totalTime;
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
}
