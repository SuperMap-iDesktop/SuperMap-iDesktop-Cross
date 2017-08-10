package com.supermap.desktop.process.tasks.events;

import com.supermap.desktop.process.tasks.ProcessWorker;
import com.supermap.desktop.process.tasks.TasksManager;

import java.util.EventObject;

/**
 * Created by highsad on 2017/7/13.
 */
public class WorkersChangedEvent extends EventObject {
	public final static int ADD = 1;
	public final static int REMOVE = 2;

	private TasksManager manager;
	private ProcessWorker worker;
	private int operation = 1;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param manager The object on which the Event initially occurred.
	 * @param worker
	 * @throws IllegalArgumentException if source is null.
	 */
	public WorkersChangedEvent(TasksManager manager, ProcessWorker worker, int operation) {
		super(manager);
		this.manager = manager;
		this.worker = worker;
		this.operation = operation;
	}

	public TasksManager getManager() {
		return manager;
	}

	public ProcessWorker getWorker() {
		return this.worker;
	}

	public int getOperation() {
		return operation;
	}
}
