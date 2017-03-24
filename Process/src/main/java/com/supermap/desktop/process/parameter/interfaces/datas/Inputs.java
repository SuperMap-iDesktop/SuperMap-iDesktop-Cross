package com.supermap.desktop.process.parameter.interfaces.datas;

import com.supermap.desktop.process.core.IProcess;

/**
 * Created by highsad on 2017/3/1.
 */
public class Inputs {
	private IProcess process;

	public Inputs() {

	}

	public void followProcess(IProcess process) {
		this.process = process;
	}

	public Object getData() {
		if (this.process != null && this.process.getOutputs() != null && this.process.getOutputs().size() > 0) {
			return this.process.getOutputs().get(0).getData();
		} else {
			return null;
		}
	}
}
