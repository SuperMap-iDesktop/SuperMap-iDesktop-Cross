package com.supermap.desktop.process.tasks;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by highsad on 2017/6/22.
 */
public abstract class Worker<V extends Object> extends SwingWorker<Boolean, V> {
	private IWorkerView<V> view;
	private String title;
	protected volatile boolean isCancelled = false;

	public Worker() {

	}

	public String getTitle() {
		return title;
	}

	public void setView(IWorkerView<V> view) {
		if (view == null) {
			throw new NullPointerException();
		}

		this.view = view;
	}

	@Override
	protected final Boolean doInBackground() {
		try {
			return doWork();
		} finally {
			if (isCancelled) {
				cancel(false);
			}
		}
	}

	protected abstract boolean doWork();

	protected void update(V chunk) {
		publish(chunk);
	}

	@Override
	protected final void process(List<V> chunks) {
		if (this.view == null) {
			return;
		}

		if (chunks != null && chunks.size() > 0) {
			this.view.update(chunks.get(0));
		}
	}

	public void cancel() {
		this.isCancelled = true;
	}

	@Override
	protected void done() {
		this.view.done();
	}
}
