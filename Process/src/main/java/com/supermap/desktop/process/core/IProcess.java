package com.supermap.desktop.process.core;

import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.parameter.IData;
import com.supermap.desktop.process.parameter.IParameters;

import javax.swing.*;

/**
 * Created by highsad on 2017/1/5.
 */
public interface IProcess {

	String getKey();

	String getTitle();

	IData getInput();

	IData getOutput();

	IParameters getParameters();

	void run();

	void addRunningListener(RunningListener listener);

	void removeRunningListener(RunningListener listener);

	JComponent getComponent();
}

