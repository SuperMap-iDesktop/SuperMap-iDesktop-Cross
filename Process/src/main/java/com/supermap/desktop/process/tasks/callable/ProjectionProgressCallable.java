package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;

/**
 * Created by xie on 2016/11/28.
 */
public class ProjectionProgressCallable extends UpdateProgressCallable {
	private IProcess transformProcess;
	private boolean isFinished = false;
	private RunningListener runningListener = new RunningListener() {
		@Override
		public void running(RunningEvent e) {
			if (e.getProgress() >= 100) {
				updateProgress(100, String.valueOf(e.getRemainTime()),  ControlsProperties.getString("String_ProjectionProgressFinished"));
			} else {
				updateProgress(100, String.valueOf(e.getRemainTime()), e.getMessage());
			}
		}
	};

	public ProjectionProgressCallable(IProcess process) {
		this.transformProcess = process;
	}

	@Override
	public Boolean call() throws Exception {
		transformProcess.addRunningListener(this.runningListener);
		transformProcess.run();
		return null;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}
}
