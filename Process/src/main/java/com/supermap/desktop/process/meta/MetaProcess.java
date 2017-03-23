package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.tasks.ProcessTask;

import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {
	protected Vector<ProcessData> outPuts;
	protected Inputs inputs;
	public ProcessTask processTask;
	protected  IParameters parameters;

	public MetaProcess() {
		this.outPuts = new Vector<>();
		this.inputs = new Inputs();
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	@Override
	public Inputs getInputs() {
		return inputs;
	}

	@Override
	public Vector<ProcessData> getOutputs() {
		return outPuts;
	}

	@Override
	public ProcessTask getProcessTask() {
		return processTask;
	}
}
