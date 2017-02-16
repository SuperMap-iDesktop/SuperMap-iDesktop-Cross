package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.IData;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

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
	public IData getInput() {
		return null;
	}

	@Override
	public IData getOutput() {
		return null;
	}
}
