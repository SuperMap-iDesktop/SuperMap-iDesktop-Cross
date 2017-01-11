package com.supermap.desktop.process.diagram.interfaces;

import com.supermap.desktop.process.core.IProcess;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IFigure {

	IProcess getProcess();

	void setProcess(IProcess process);

	IAttribute getAttribute();

	void setAttribute(IAttribute attribute);
}
