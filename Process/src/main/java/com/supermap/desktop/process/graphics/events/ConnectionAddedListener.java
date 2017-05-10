package com.supermap.desktop.process.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/5/8.
 */
public interface ConnectionAddedListener extends EventListener {
	void connectionAdded(ConnectionAddedEvent e);
}
