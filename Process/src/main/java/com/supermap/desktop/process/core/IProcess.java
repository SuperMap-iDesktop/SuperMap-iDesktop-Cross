package com.supermap.desktop.process.core;

import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcess {

	RunningStatus getStatus();

	String getKey();

	IProcessGroup getParent();

	void setParent(ProcessGroup parent);

	String getTitle();

	Inputs getInputs();

	Outputs getOutputs();

	IParameters getParameters();

	void run();

	void cancel();

	boolean isCancelled();

	void reset();

	void addRunningListener(RunningListener listener);

	void removeRunningListener(RunningListener listener);

	void addStatusChangeListener(StatusChangeListener listener);

	void removeStatusChangeListener(StatusChangeListener listener);

	IParameterPanel getComponent();

	String toXml();

	void fromXml(String xml);
}

