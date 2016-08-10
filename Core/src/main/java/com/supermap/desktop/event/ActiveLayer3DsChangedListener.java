package com.supermap.desktop.event;

import java.util.EventListener;

public interface ActiveLayer3DsChangedListener extends EventListener {
	void activeLayer3DsChanged(ActiveLayer3DsChangedEvent e);
}
