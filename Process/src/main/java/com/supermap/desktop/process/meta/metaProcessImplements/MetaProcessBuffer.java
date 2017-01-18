package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.IParameters;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;

import javax.swing.*;

/**
 * @author XiaJT
 */
public class MetaProcessBuffer extends MetaProcess {

	private IParameters parameters;

	public MetaProcessBuffer() {
		parameters = new DefaultParameters();

	}

	@Override
	public JComponent getComponent() {
		return parameters.getPanel();
	}

	@Override
	public void run() {

	}
}
