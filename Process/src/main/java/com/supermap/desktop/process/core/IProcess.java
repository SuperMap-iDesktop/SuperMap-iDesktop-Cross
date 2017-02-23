package com.supermap.desktop.process.core;

import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.parameter.interfaces.IData;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcess {

	String getKey();

	String getTitle();

	Vector<IData> getInputs();

	Vector<IData> getOutputs();

	IParameters getParameters();

	void run();

	void addRunningListener(RunningListener listener);

	void removeRunningListener(RunningListener listener);

	JComponent getComponent();
}

