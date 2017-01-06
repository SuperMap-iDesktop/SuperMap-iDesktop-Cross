package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/1/5.
 */
public interface RunningListener extends EventListener {
	void running(RunningEvent e);
}
