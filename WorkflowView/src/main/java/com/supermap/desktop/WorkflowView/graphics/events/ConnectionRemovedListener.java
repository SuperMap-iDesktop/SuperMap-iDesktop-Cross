package com.supermap.desktop.WorkflowView.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/5/8.
 */
public interface ConnectionRemovedListener extends EventListener {
	void connectionRemoved(ConnectionRemovedEvent e);
}
