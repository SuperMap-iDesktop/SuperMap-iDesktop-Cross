package com.supermap.desktop.process.tasks;

/**
 * Created by highsad on 2017/6/22.
 */
public interface IWorkerView<V> {
	Worker<V> getWorker();

	boolean cancel();

	void update(V chunk);

	void close();
}
