package com.supermap.desktop.event;

import com.supermap.desktop.Interface.IWorkFlow;

/**
 * @author XiaJT
 */
public class WorkFlowChangedEvent {
	private IWorkFlow[] workFlows;
	private int type;

	public static final int ADD = 1;
	public static final int DELETE = 2;
	public static final int MODIFIED = 3;
	public static final int RE_BUILD = 4;

	public WorkFlowChangedEvent(int type, IWorkFlow... workFlows) {
		this.workFlows = workFlows;
		this.type = type;
	}

	public IWorkFlow[] getWorkFlows() {
		return workFlows;
	}

	public void setWorkFlows(IWorkFlow[] workFlows) {
		this.workFlows = workFlows;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
