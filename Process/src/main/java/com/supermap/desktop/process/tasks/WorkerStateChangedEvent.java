package com.supermap.desktop.process.tasks;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/27.
 */
public class WorkerStateChangedEvent extends EventObject {
	private TasksManager manager;
	private ProcessWorker processWorker;
	private int oldState;
	private int newState;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param manager The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public WorkerStateChangedEvent(TasksManager manager, ProcessWorker processWorker, int oldState, int newState) {
		super(manager);
		this.manager = manager;
		this.processWorker = processWorker;
		this.oldState = oldState;
		this.newState = newState;
	}

	public TasksManager getManager() {
		return manager;
	}

	public ProcessWorker getProcessWorker() {
		return processWorker;
	}

	public int getOldState() {
		return oldState;
	}

	public int getNewState() {
		return newState;
	}
}
