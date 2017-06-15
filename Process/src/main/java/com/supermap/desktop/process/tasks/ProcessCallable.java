package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.progress.Interface.UpdateProgressCallable;

import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2017/4/11.
 * Refresh gui callable
 */
public class ProcessCallable extends UpdateProgressCallable {

	private IProcess process;
	private RunningListener runningListener = new RunningListener() {
		@Override
		public void running(RunningEvent e) {
			try {
				updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
			} catch (CancellationException ex) {
				e.setCancel(true);
			}
		}
	};

	public ProcessCallable(IProcess process) {
		this.process = process;
	}

	@Override
	public Boolean call() {
		try {
			this.process.addRunningListener(this.runningListener);
			this.process.run();

		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			this.process.removeRunningListener(this.runningListener);
		}
		return false;
	}
}
