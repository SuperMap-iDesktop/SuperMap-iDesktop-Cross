package com.supermap.desktop.process.tasks.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/6/27.
 */
public interface WorkerStateChangedListener extends EventListener {
	void workerStateChanged(WorkerStateChangedEvent e);
}
