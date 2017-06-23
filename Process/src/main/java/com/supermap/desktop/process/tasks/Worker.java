package com.supermap.desktop.process.tasks;

import javax.swing.*;
import java.util.List;

/**
 * Created by highsad on 2017/6/22.
 */
public abstract class Worker<V extends Object> extends SwingWorker<Boolean, V> {
	private IWorkerView<V> view;
	private String title;

	public Worker(IWorkerView<V> view) {
		if (view == null) {
			throw new NullPointerException();
		}

		this.view = view;
	}

	public String getTitle() {
		return title;
	}

	@Override
	protected final Boolean doInBackground() {
		return doWork();
	}

	protected abstract boolean doWork();

	protected void update(V chunk) {
		publish(chunk);
	}

	@Override
	protected final void process(List<V> chunks) {
		if (chunks != null && chunks.size() > 0) {
			this.view.update(chunks.get(0));
		}
	}

	@Override
	protected void done() {
		this.view.close();
	}
}
