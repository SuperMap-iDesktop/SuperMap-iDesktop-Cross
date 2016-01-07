package com.supermap.desktop.event;

import java.util.EventListener;

public interface ActiveLayersChangedListener extends EventListener {
	void acitiveLayersChanged(ActiveLayersChangedEvent e);
}
