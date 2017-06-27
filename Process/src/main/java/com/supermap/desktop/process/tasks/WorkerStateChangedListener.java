package com.supermap.desktop.process.tasks;

import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/6/27.
 */
public interface WorkerStateChangedListener extends ActionListener {
	void workerStateChanged(WorkerStateChangedEvent e);
}
