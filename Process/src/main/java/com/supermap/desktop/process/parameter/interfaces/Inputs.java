package com.supermap.desktop.process.parameter.interfaces;

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
		return this.process.getOutputs().get(0).getData();
	}
}
