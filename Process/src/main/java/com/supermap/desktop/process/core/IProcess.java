package com.supermap.desktop.process.core;

import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.ProcessData;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.tasks.ProcessTask;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcess {

	String getKey();

	IProcessGroup getParent();

	void setParent(ProcessGroup parent);

	String getTitle();

	Inputs getInputs();

	Vector<ProcessData> getOutputs();

	IParameters getParameters();

	void run();

	Icon getIcon();

	void addRunningListener(RunningListener listener);

	void removeRunningListener(RunningListener listener);

	IParameterPanel getComponent();

	 ProcessTask getProcessTask();
}

