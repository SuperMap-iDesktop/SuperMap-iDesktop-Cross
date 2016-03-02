package com.supermap.desktop.event;

import java.util.EventListener;

public interface SelectedChangeListener extends EventListener {
	void selectedChange(SelectedChangeEvent e);
}
