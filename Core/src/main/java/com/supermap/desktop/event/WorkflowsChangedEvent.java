package com.supermap.desktop.event;

/**
 * @author XiaJT
 */
public class WorkflowsChangedEvent {
	private String[] workflowNames;
	private int type;

	public static final int ADD = 1;
	public static final int DELETE = 2;
	public static final int MODIFIED = 3;
	public static final int RE_BUILD = 4;

	public WorkflowsChangedEvent(int type, String... workflowNames) {
		this.workflowNames = workflowNames;
		this.type = type;
	}

	public String[] getWorkflowNames() {
		return this.workflowNames;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
