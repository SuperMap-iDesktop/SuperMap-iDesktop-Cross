package com.supermap.desktop.process.core;

import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.parameter.interfaces.IData;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author XiaJT
 */
public class ProcessGroup implements IProcessGroup, IProcess {

	private String name;
	private ProcessGroup parent;
	private ArrayList<IProcess> processes = new ArrayList<>();

	@Override
	public int getChildCount() {
		return processes.size();
	}

	@Override
	public IProcess getProcessByIndex(int index) {
		return processes.get(index);
	}

	@Override
	public IProcess getProcessByKey(String key) {
		for (IProcess process : processes) {
			if (process instanceof ProcessGroup) {
				IProcess result = ((ProcessGroup) process).getProcessByKey(key);
				if (result != null) {
					return result;
				}
			} else if (process.getKey().equals(key)) {
				return process;
			}
		}
		return null;
	}

	@Override
	public void setName(String name) {
		if (parent == null) {
			this.name = name;
		} else if (parent.isLegitName(name)) {

		}
	}

	@Override
	public boolean isLegitName(String name) {
		return false;
	}

	//region 无用方法
	@Override
	public String getKey() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public Vector<IData> getInputs() {
		return null;
	}

	@Override
	public Vector<IData> getOutputs() {
		return null;
	}

	@Override
	public IParameters getParameters() {
		return null;
	}

	@Override
	public void run() {

	}

	@Override
	public void addRunningListener(RunningListener listener) {

	}

	@Override
	public void removeRunningListener(RunningListener listener) {

	}

	@Override
	public JComponent getComponent() {
		return null;
	}
	//endregion
}
