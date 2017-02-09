package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;

/**
 * Created by xie on 2016/11/28.
 */
public class SpatialIndexProgressCallable extends UpdateProgressCallable {

	protected IProcess spatialIndexProcess;
	private boolean isFinished = false;
	private RunningListener runningListener = new RunningListener() {
		@Override
		public void running(RunningEvent e) {
			if (e.getProgress() >= 100) {
				updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_SpatialIndexProgressFinished"));
			} else {
				updateProgress(100, String.valueOf(e.getRemainTime()), e.getMessage());
			}
		}
	};


	public SpatialIndexProgressCallable(IProcess process) {
		this.spatialIndexProcess = process;
	}

	@Override
	public Boolean call() throws Exception {
		spatialIndexProcess.addRunningListener(this.runningListener);
		spatialIndexProcess.run();
		return null;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}
}
