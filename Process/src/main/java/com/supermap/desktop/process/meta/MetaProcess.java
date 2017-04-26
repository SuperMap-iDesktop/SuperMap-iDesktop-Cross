package com.supermap.desktop.process.meta;

import com.supermap.desktop.process.ProcessResources;
import com.supermap.desktop.process.core.AbstractProcess;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class MetaProcess extends AbstractProcess {
	protected IParameters parameters;
	protected boolean finished = false;


	public MetaProcess() {
	}

	@Override
	public IParameters getParameters() {
		return parameters;
	}

	protected Icon getIconByPath(String path) {
		return new ImageIcon(ProcessResources.getResourceURL(path));
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}
}
