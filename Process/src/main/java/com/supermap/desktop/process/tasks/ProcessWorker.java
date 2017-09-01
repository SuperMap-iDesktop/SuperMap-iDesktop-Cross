package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.properties.CoreProperties;

import java.text.MessageFormat;

/**
 * Created by highsad on 2017/6/22.
 */
public class ProcessWorker {
	public Worker<SingleProgress> worker;
	private IProcess process;
	private RunningHandler runningHandler = new RunningHandler();
	private IWorkerView view;

	public ProcessWorker(IProcess process) {
		if (process == null) {
			throw new NullPointerException();
		}

		this.process = process;
		this.process.addRunningListener(this.runningHandler);
	}

	public IProcess getProcess() {
		return this.process;
	}


	public void execute() {
		if (worker != null) {
			worker.cancel();
		}
		worker = new Worker<SingleProgress>() {
			@Override
			protected boolean doWork() {
				return ProcessWorker.this.process.run();
			}
		};
		worker.setTitle(process.getTitle());
		worker.setView(view);
		worker.execute();
	}

	public void setView(IWorkerView view) {
		this.view = view;
	}

	public void cancel() {
		worker.cancel();
	}


	private class RunningHandler implements RunningListener {
		@Override
		public void running(RunningEvent e) {
			try {
				if (worker.isCancelled()) {
					e.setCancel(true);
				} else {
					if (e.isIndeterminate()) {
						worker.update(new SingleProgress(e.getMessage()));
					} else {
						worker.update(new SingleProgress(e.getProgress(), e.getMessage(), MessageFormat.format(CoreProperties.getString("String_RemainTime"), e.getRemainTime())));
					}
				}
			} catch (Exception e1) {
				e.setCancel(true);
				Application.getActiveApplication().getOutput().output(e1);
			}
		}
	}
}
