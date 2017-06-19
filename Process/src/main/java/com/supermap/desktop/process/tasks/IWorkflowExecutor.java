package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Interface.IWorkflow;

/**
 * Created by highsad on 2017/6/15.
 */
public interface IWorkflowExecutor {
	int getState();

	void loadWorkflow(IWorkflow workflow);

	void excute();
}
