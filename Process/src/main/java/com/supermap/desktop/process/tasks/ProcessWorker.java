package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.progress.Interface.IUpdateProgress;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Created by highsad on 2017/6/14.
 */
public class ProcessWorker extends SwingWorker<Boolean, Object> {
	private IUpdateProgress update;
	private IProcess process;
	private RunningListener runningListener = new RunningListener() {
		@Override
		public void running(RunningEvent e) {

		}
	};

	private ProcessWorkerPropertyChange propertyChange = new ProcessWorkerPropertyChange();

	public ProcessWorker(IProcess process) {
		this.process = process;
		addPropertyChangeListener(this.propertyChange);
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		boolean ret = true;
		try {
			this.process.addRunningListener(this.runningListener);
			this.process.run();
		} catch (Exception e) {
			ret = false;
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.process.removeRunningListener(this.runningListener);
		}
		return ret;
	}

	@Override
	protected void process(List<Object> chunks) {
		super.process(chunks);
	}

	@Override
	protected void done() {
		super.done();
	}

	private class ProcessWorkerPropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

		}
	}
}
