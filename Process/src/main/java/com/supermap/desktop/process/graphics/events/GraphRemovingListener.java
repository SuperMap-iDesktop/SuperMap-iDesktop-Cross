package com.supermap.desktop.process.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/5/27.
 */
public interface GraphRemovingListener extends EventListener {
	void graphRemoving(GraphRemovingEvent e);
}
