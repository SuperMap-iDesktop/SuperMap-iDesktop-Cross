package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {

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
		return null;
	}

	@Override
	public Vector<ProcessData> getOutputs() {
		return null;
	}
}
