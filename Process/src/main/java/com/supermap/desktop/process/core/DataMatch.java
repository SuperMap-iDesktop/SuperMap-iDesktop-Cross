package com.supermap.desktop.process.core;

import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.utilities.StringUtilities;
import org.apache.commons.lang.NullArgumentException;

/**
 * Created by highsad on 2017/6/29.
 */
public class DataMatch implements IRelation<IProcess> {
	private IProcess from;
	private IProcess to;

	private InputData toInputData;
	private OutputData fromOutputData;

	public DataMatch(IProcess from, IProcess to, String fromDataName, String toDataName) {
		if (from == null || to == null || StringUtilities.isNullOrEmpty(fromDataName) || StringUtilities.isNullOrEmpty(toDataName)) {
			throw new NullArgumentException("parameter can not be null.");
		}

		if (from == to) {
			throw new UnsupportedOperationException();
		}

		if (!from.getOutputs().isContains(fromDataName) || !to.getInputs().isContains(toDataName)) {
			throw new UnsupportedOperationException();
		}

		this.from = from;
		this.to = to;

		this.fromOutputData = this.from.getOutputs().getData(fromDataName);
		this.toInputData = this.to.getInputs().getData(toDataName);
		this.toInputData.bind(this.fromOutputData);
	}

	@Override
	public IProcess getFrom() {
		return this.from;
	}

	@Override
	public IProcess getTo() {
		return to;
	}

	public InputData getToInputData() {
		return toInputData;
	}

	public OutputData getFromOutputData() {
		return fromOutputData;
	}

	@Override
	public void clear() {
		this.toInputData.unbind();
		this.toInputData = null;
		this.fromOutputData = null;
		this.from = null;
		this.to = null;
	}
}
