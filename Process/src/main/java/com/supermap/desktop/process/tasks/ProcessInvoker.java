package com.supermap.desktop.process.tasks;

import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by highsad on 2017/6/15.
 */
public class ProcessInvoker {
	private IProcess process;
	private List<IProcess> requiredList = new ArrayList<>();
	private RequredStatusChangeListener statusChangeListener = new RequredStatusChangeListener();

	public ProcessInvoker(IProcess process) {
		this.process = process;
	}

	public IProcess getProcess() {
		return process;
	}

	public void addRequired(IProcess process) {
		synchronized (this.requiredList) {
			if (process != null && !this.requiredList.contains(process)) {
				process.addStatusChangeListener(this.statusChangeListener);
				this.requiredList.add(process);
			}
		}
	}

	public void removeRequired(IProcess process) {
		synchronized (this.requiredList) {
			if (process != null && this.requiredList.contains(process)) {
				process.removeStatusChangeListener(this.statusChangeListener);
				this.requiredList.remove(process);
			}
		}
	}

	private class RequredStatusChangeListener implements StatusChangeListener {
		@Override
		public void statusChange(StatusChangeEvent e) {
			if (e.getStatus() == RunningStatus.COMPLETED) {
				removeRequired(e.getProcess());
			} else if (e.getStatus() == RunningStatus.RUNNING) {
				// nothing to do
			} else if (e.getStatus() == RunningStatus.CANCELLED) {
				// nothing to do
			} else if (e.getStatus() == RunningStatus.EXCEPTION) {
				// nothing to do
			}
		}
	}
}
