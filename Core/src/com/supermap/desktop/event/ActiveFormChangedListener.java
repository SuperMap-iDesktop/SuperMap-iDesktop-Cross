package com.supermap.desktop.event;

import java.util.EventListener;

public interface ActiveFormChangedListener extends EventListener {
	void activeFormChanged(ActiveFormChangedEvent e);
}
