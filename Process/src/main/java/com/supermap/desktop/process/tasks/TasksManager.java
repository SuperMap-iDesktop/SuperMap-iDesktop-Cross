package com.supermap.desktop.process.tasks;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.MatrixExecutor;
import com.supermap.desktop.process.core.Workflow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by highsad on 2017/6/14.
 */
public class TasksManager {
	private Workflow workflow;
	private Map<IProcess, ProcessTask> taskMap = new ConcurrentHashMap<>();

	public TasksManager(Workflow workflow) {
		this.workflow = workflow;
	}

	public boolean execute() {
		MatrixExecutor executor = new MatrixExecutor(workflow.getMatrix());
		return false;
	}
}
