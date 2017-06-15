package com.supermap.desktop.process.events;

import com.supermap.desktop.process.core.Workflow;

import java.util.EventObject;

/**
 * Created by highsad on 2017/6/15.
 */
public class WorkflowChangeEvent extends EventObject {
	public final static int NOTHING = 0;
	public final static int ADDING = 1;
	public final static int ADDED = 2;
	public final static int REMOVING = 3;
	public final static int REMOVED = 4;

	private int type = NOTHING;
	private Workflow workflow;
	private Process process;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public WorkflowChangeEvent(Workflow source, int type, Process data) {
		super(source);
		this.workflow = source;
		this.type = type;
		this.process = data;
	}

	public int getType() {
		return type;
	}

	public Process getProcess() {
		return process;
	}

	public Workflow getWorkflow() {
		return workflow;
	}
}
