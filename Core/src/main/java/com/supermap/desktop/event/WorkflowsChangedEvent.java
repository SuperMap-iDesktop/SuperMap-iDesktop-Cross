package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IDataEntry;

/**
 * @author XiaJT
 */
public class WorkflowsChangedEvent {
	private IDataEntry<String>[] workflowEntries;
	private int type;

	public static final int ADD = 1;
	public static final int DELETE = 2;
	public static final int MODIFIED = 3;
	public static final int RE_BUILD = 4;

	public WorkflowsChangedEvent(int type, IDataEntry<String>... workflowEntries) {
		this.workflowEntries = workflowEntries;
		this.type = type;
	}

	public IDataEntry<String>[] getWorkflowEntries() {
		return this.workflowEntries;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
