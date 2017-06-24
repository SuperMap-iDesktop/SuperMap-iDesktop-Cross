package com.supermap.desktop.process.tasks;

/**
 * Created by highsad on 2017/6/22.
 */
public interface IWorkerView<V> {
	void execute(Worker<V> worker);

	void update(V chunk);

	void done();
}
