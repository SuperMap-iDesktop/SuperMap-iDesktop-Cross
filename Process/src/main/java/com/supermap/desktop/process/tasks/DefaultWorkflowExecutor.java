package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Interface.IWorkflow;

/**
 * Created by highsad on 2017/6/15.
 */
public class DefaultWorkflowExecutor implements IWorkflowExecutor {
	private IWorkflow workflow;


	@Override
	public int getState() {
		return 0;
	}

	@Override
	public void loadWorkflow(IWorkflow workflow) {
		this.workflow = workflow;
		analyzeTasks();
	}

	private void analyzeTasks() {

	}

	@Override
	public void excute() {

	}
}
