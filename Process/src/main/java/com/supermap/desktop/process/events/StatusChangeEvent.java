package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.enums.RunningStatus;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/15.
 */
public class StatusChangeEvent extends EventObject {
	private RunningStatus status = RunningStatus.NORMAL;
	private RunningStatus oldStatus = RunningStatus.NORMAL;
	private IProcess process;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public StatusChangeEvent(IProcess source, RunningStatus status, RunningStatus oldStatus) {
		super(source);
		this.process = source;
		this.status = status;
		this.oldStatus = oldStatus;

	}

	public RunningStatus getStatus() {
		return status;
	}

	public RunningStatus getOldStatus() {
		return oldStatus;
	}

	public IProcess getProcess() {
		return process;
	}
}
