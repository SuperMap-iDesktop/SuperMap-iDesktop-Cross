package com.supermap.desktop.process.tasks;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by highsad on 2017/9/20.
 */
public abstract class Worker<V extends Object> {
	private IWorkerView<V> view;
	private volatile boolean isCancelled = false;
	private volatile boolean isRunning = false;
	private String title;
	private SwingWorkerSub workerSub;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setView(IWorkerView<V> view) {
		if (view == null) {
			throw new NullPointerException();
		}

		this.view = view;
	}

	public boolean get() throws ExecutionException, InterruptedException {
		if (this.workerSub == null) {
			throw new IllegalArgumentException();
		}

		return this.workerSub.get() == null ? false : this.workerSub.get();
	}

	public void cancel() {
		this.isCancelled = true;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public final void execute() {
		if (this.workerSub != null) {
			cancel();
			try {

				// 等待取消完成
				this.workerSub.get();
				this.isCancelled = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		// 由于 SwingWorker 不支持重新执行，因此如果需要重复执行请构造一个新的 SwingWOrker
		this.isRunning = true;
		this.workerSub = new SwingWorkerSub();
		this.workerSub.execute();
	}

	protected abstract boolean doWork();

	protected void update(V chunk) {
		if (this.workerSub != null) {
			this.workerSub.update(chunk);
		}
	}

	private class SwingWorkerSub extends SwingWorker<Boolean, V> {

		@Override
		protected final Boolean doInBackground() {
			try {
				return doWork();
			} catch (Exception e) {
				return false;
			} finally {

				// SwingWorker 的原生 cancel 之后会立即调用 done 方法
				// 而很多功能取消都会有数据回滚等需要等待的操作
				// 因此自定义 cancel 的实现，并在 doWork 线程中自行处理 cancel
				// 然后再调用 SwingWorker 的原生 cancel 操作，设置 SwingWorker 的状态
				// 最后才会调用 done 方法，此时无论自定义的 cancel 状态还是 SwingWorker 的
				// cancel 状态都是正确的
				if (isCancelled()) {
					cancel(false);
				}
			}
		}

		protected void update(V chunk) {
			publish(chunk);
		}

		@Override
		protected final void process(List<V> chunks) {
			if (view == null) {
				return;
			}

			if (chunks != null && chunks.size() > 0) {
				view.update(chunks.get(chunks.size() - 1));
			}
		}

		@Override
		protected void done() {
			view.done();
			isRunning = false;
		}
	}
}
