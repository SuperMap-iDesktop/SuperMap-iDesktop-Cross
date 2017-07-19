package com.supermap.desktop.process.tasks.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/7/13.
 */
public interface WorkersChangedListener extends EventListener {
	void workersChanged(WorkersChangedEvent e);
}
