package com.supermap.desktop.event;

import java.util.EventListener;

/**
 * Created by highsad on 2016/12/15.
 */
public interface DockbarClosingListener extends EventListener {
	void dockbarClosing(DockbarClosingEvent e);
}

