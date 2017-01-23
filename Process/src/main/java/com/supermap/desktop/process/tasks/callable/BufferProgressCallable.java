package com.supermap.desktop.process.tasks.callable;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;

/**
 * Created by xie on 2016/11/28.
 */
public class BufferProgressCallable extends UpdateProgressCallable {
	private IProcess bufferProgressProcess;
	private boolean isFinished = false;
	private RunningListener runningListener = new RunningListener() {
		@Override
		public void running(RunningEvent e) {
			if (e.getProgress() >= 100) {
				updateProgress(100, String.valueOf(e.getRemainTime()), ControlsProperties.getString("String_BufferProgressFinished"));
			} else {
				updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
			}
		}
	};

	public BufferProgressCallable(IProcess process) {
		this.bufferProgressProcess = process;
	}

	@Override
	public Boolean call() throws Exception {
		bufferProgressProcess.addRunningListener(this.runningListener);
		bufferProgressProcess.run();
		return false;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}
}
