package com.supermap.desktop.ui.mdi.events;

import java.util.EventListener;

/**
 * Created by highsad on 2016/9/22.
 */
public interface PageClosedListener extends EventListener {
	void pageRemoved(PageClosedEvent e);
}
