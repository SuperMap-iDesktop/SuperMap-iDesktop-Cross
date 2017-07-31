package com.supermap.desktop.WorkflowView.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/5/8.
 */
public interface ConnectionRemovingListener extends EventListener {
	void connectionRemoving(ConnectionRemovingEvent e);
}
