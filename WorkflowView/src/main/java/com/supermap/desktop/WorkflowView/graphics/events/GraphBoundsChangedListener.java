package com.supermap.desktop.WorkflowView.graphics.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/3/21.
 */
public interface GraphBoundsChangedListener extends EventListener {
	void graghBoundsChanged(GraphBoundsChangedEvent e);
}
