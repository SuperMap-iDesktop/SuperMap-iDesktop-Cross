package com.supermap.desktop.process.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/3/8.
 */
public interface GraphCreatedListener extends EventListener {
	void graphCreated(GraphCreatedEvent e);
}
