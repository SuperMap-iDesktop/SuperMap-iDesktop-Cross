package com.supermap.desktop.process.core;

import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;

import javax.swing.event.EventListenerList;

/**
 * Created by highsad on 2017/1/5.
 */
public abstract class AbstractProcess implements IProcess {

	private volatile RunningStatus status = RunningStatus.NORMAL;
	private EventListenerList listenerList = new EventListenerList();
	protected IProcessGroup parent;

	@Override
	public abstract IParameters getParameters();

	@Override
	public synchronized final boolean run() {
		boolean isSuccessful = false;

		try {
			if (this.status != RunningStatus.NORMAL) {
				return false;
			}

			setStatus(RunningStatus.RUNNING);
			isSuccessful = execute();

			if (isSuccessful) {
				setStatus(RunningStatus.COMPLETED);
			} else if (!isCancelled()) {
				setStatus(RunningStatus.EXCEPTION);
			}
		} catch (Exception e) {
			setStatus(RunningStatus.EXCEPTION);
		}
		return isSuccessful;
	}

	@Override
	public final void cancel() {
		if (this.status != RunningStatus.NORMAL || this.status == RunningStatus.CANCELLED) {
			return;
		}

		setStatus(RunningStatus.CANCELLED);
	}

	@Override
	public final boolean isCancelled() {
		return this.status == RunningStatus.CANCELLED;
	}

	protected final void setStatus(RunningStatus status) {
		if (this.status != status) {
			RunningStatus oldStatus = this.status;
			this.status = status;
			fireStatusChange(new StatusChangeEvent(this, this.status, oldStatus));
		}
	}

	public abstract boolean execute();

	@Override
	public void reset() {
		RunningStatus oldStatus = this.status;

		if (oldStatus != RunningStatus.NORMAL) {
			this.status = RunningStatus.NORMAL;
			fireStatusChange(new StatusChangeEvent(this, RunningStatus.NORMAL, oldStatus));
		}
	}

	@Override
	public RunningStatus getStatus() {
		return this.status;
	}

	@Override
	public abstract String getKey();

	@Override
	public Inputs getInputs() {
		return this.getParameters().getInputs();
	}


	@Override
	public Outputs getOutputs() {
		return this.getParameters().getOutputs();
	}

	@Override
	public void addRunningListener(RunningListener listener) {
		this.listenerList.add(RunningListener.class, listener);
	}

	@Override
	public void removeRunningListener(RunningListener listener) {
		this.listenerList.remove(RunningListener.class, listener);
	}

	@Override
	public void addStatusChangeListener(StatusChangeListener listener) {
		this.listenerList.add(StatusChangeListener.class, listener);
	}

	@Override
	public void removeStatusChangeListener(StatusChangeListener listener) {
		this.listenerList.remove(StatusChangeListener.class, listener);
	}

	protected void fireRunning(RunningEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == RunningListener.class) {
				((RunningListener) listeners[i + 1]).running(e);
			}
		}
	}

	protected void fireStatusChange(StatusChangeEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == StatusChangeListener.class) {
				((StatusChangeListener) listeners[i + 1]).statusChange(e);
			}
		}
	}

	@Override
	public IProcessGroup getParent() {
		return parent;
	}

	@Override
	public void setParent(ProcessGroup parent) {
		this.parent = parent;
	}

	@Override
	public String toXml() {
		return "Key = " + getKey();
	}

	@Override
	public void fromXml(String xml) {

	}
}
