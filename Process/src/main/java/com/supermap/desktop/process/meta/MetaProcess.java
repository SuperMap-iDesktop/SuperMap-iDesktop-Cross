package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;

import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {
	protected Vector<ProcessData> outPuts;
	protected Vector<ProcessData> inputs;

	@Override

	public IParameters getParameters() {
		return null;
	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public Vector<ProcessData> getInputs() {
		return inputs;
	}

	@Override
	public Vector<ProcessData> getOutputs() {
		return outPuts;
	}

	@Override
	public void setInputs(Vector<ProcessData> inputs) {
		this.inputs = inputs;
	}
}
