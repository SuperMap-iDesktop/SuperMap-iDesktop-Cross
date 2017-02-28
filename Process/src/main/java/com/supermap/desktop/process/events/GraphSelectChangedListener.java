package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/2/28.
 */
public interface GraphSelectChangedListener extends EventListener {
	void GraphSelectChanged(GraphSelectedChangedEvent e);
}
