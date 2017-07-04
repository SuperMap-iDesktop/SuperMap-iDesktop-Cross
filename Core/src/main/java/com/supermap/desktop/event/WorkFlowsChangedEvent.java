package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IWorkflow;

/**
 * @author XiaJT
 */
public class WorkFlowsChangedEvent {
	private IWorkflow[] workFlows;
	private int type;

	public static final int ADD = 1;
	public static final int DELETE = 2;
	public static final int MODIFIED = 3;
	public static final int RE_BUILD = 4;

	public WorkFlowsChangedEvent(int type, IWorkflow... workFlows) {
		this.workFlows = workFlows;
		this.type = type;
	}

	public IWorkflow[] getWorkFlows() {
		return workFlows;
	}

	public void setWorkFlows(IWorkflow[] workFlows) {
		this.workFlows = workFlows;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
