package com.supermap.desktop.process.tasks.taskStates;

import com.supermap.desktop.process.core.IProcess;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XiaJT
 */
public class TaskState {
	private int stateIndex;
	private String name;
	private CopyOnWriteArrayList<IProcess> processes = new CopyOnWriteArrayList<>();

	public TaskState(int stateIndex, String name) {
		this.stateIndex = stateIndex;
		this.name = name;
	}

	public boolean moveProcessTo(IProcess process, TaskState state) {
		try {
			processes.remove(process);
			state.processes.add(process);
		} catch (Exception e) {
			processes.remove(process);
			state.processes.remove(process);
			return false;
		}
		return true;
	}

	public void addProcess(IProcess process) {
		if (!processes.contains(process)) {
			processes.add(process);
		}
	}

	public CopyOnWriteArrayList<IProcess> getProcesses() {
		return processes;
	}

	public boolean contain(IProcess process) {
		return processes.contains(process);
	}

	public void removeProcess(IProcess process) {
		processes.remove(process);
	}

	public int getStateIndex() {
		return stateIndex;
	}

	public void setStateIndex(int stateIndex) {
		this.stateIndex = stateIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
