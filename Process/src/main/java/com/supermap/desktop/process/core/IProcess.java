package com.supermap.desktop.process.core;

import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcess {

	RunningStatus getStatus();

	Workflow getWorkflow();

	void setWorkflow(Workflow workflow);

	String getKey();

	int getSerialID();

	void setSerialID(int serialID);

	String getTitle();

	Inputs getInputs();

	Outputs getOutputs();

	IParameters getParameters();

	boolean run();

	boolean isReady();

	boolean checkReadyState();

	void cancel();

	boolean isCancelled();

	void setStatus(RunningStatus status);

	void reset();

	Class<? extends IProcessLoader> getLoader();

	void addProcessReadyChecker(IReadyChecker<IProcess> processReadyChecker);

	void removeProcessReadyChecker(IReadyChecker<IProcess> processReadyChecker);

	void addRunningListener(RunningListener listener);

	void removeRunningListener(RunningListener listener);

	void addStatusChangeListener(StatusChangeListener listener);

	void removeStatusChangeListener(StatusChangeListener listener);

	IParameterPanel getComponent();

	boolean isChangeSourceData();
}

